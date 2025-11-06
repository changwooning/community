package com.example.community.service;

import com.example.community.dto.board.BoardDetailResponseDto;
import com.example.community.dto.board.BoardListResponseDto;
import com.example.community.dto.board.BoardRequestDto;
import com.example.community.dto.board.BoardResponseDto;
import com.example.community.dto.comment.CommentResponseDto;
import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.enums.SortType;
import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.InvalidBoardException;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

  private final UserRepository userRepository;
  private final BoardRepository boardRepository;
  private final CommentService commentService;
  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;


  // 게시글 작성
  public BoardResponseDto createBoard(Long userId, BoardRequestDto requestDto) {

    // 작성자 검증
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("작성자 정보를 찾을 수 없습니다."));

    validateBoard(requestDto);

    // 게시글 생성
    Board board = Board.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .user(user)
        .build();

    Board savedBoard = boardRepository.save(board);

    return BoardResponseDto.builder()
        .id(savedBoard.getId())
        .title(savedBoard.getTitle())
        .content(savedBoard.getContent())
        .nickName(user.getNickName())
        .views(savedBoard.getViews())
        .createdAt(savedBoard.getCreatedAt())
        .build();
  }

  // 게시글 목록 조회
  public Page<BoardListResponseDto> getBoardList(Pageable pageable, SortType sortBy) {

    PageRequest pageRequest = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(Direction.DESC, sortBy.getField())
    );

    return boardRepository.findAll(pageRequest).map(BoardListResponseDto::from);
  }

  // 특정 게시글 조회
  @Transactional(readOnly = true)
  public BoardDetailResponseDto getBoardDetail(Long boardId, Pageable pageable) {

    final String cacheKey = "board:detail:" + boardId;
    final String viewKey = "board:" + boardId + ":views";

    // 1) 캐시 HIT 시도
    String cached = redisTemplate.opsForValue().get(cacheKey);
    if (cached != null) {
      try {
        BoardDetailResponseDto detailDto = objectMapper.readValue(cached,
            BoardDetailResponseDto.class);
        // 역직렬화 성공일 때만 카운트
        redisTemplate.opsForValue().increment(viewKey);
        return detailDto;
      } catch (Exception e) {
        // 캐시 손상/포맷 변경/워밍 레이스 -> 서비스는 DB 폴백
        log.warn("Cache corrupted for key={}, fallback to DB", cacheKey, e);
      }
    }

    // 2) 캐시 MISS 또는 손상 시 DB 조회
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new BoardNotFoundException("해당 게시글을 찾을 수 없습니다."));

    Page<CommentResponseDto> comments = commentService.getComments(boardId, pageable);
    BoardDetailResponseDto detailResponse = BoardDetailResponseDto.from(board, comments);

    // 3) 캐시 저장 (실패해도 서비스 계속)
    try {
      String json = objectMapper.writeValueAsString(detailResponse);
      redisTemplate.opsForValue().set(cacheKey, json, Duration.ofSeconds(30));
    } catch (Exception e) {
      log.warn("Cache serialization failed for key={}", cacheKey, e);
    }

    // 4) 조회수 증가 (Redis only)
    redisTemplate.opsForValue().increment(viewKey);

    return detailResponse;
  }

  private void validateBoard(BoardRequestDto requestDto) {
    if (requestDto.getTitle() == null || requestDto.getTitle().trim().isEmpty()) {
      throw new InvalidBoardException("게시글 제목이 비어있습니다.");
    }

    if (requestDto.getContent() == null || requestDto.getContent().trim().isEmpty()) {
      throw new InvalidBoardException("게시글 내용이 비어있습니다.");
    }

  }


}
