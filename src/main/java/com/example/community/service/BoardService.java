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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final UserRepository userRepository;
  private final BoardRepository boardRepository;
  private final CommentService commentService;


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
  @Transactional
  public BoardDetailResponseDto getBoardDetail(Long boardId, Pageable pageable) {
    // 1. 게시글 조회 (비관적 락 적용하기)
    Board board = boardRepository.findByIdWithLock(boardId)
        .orElseThrow(() -> new BoardNotFoundException("해당 게시글이 존재하지 않습니다."));

    // 2. 조회수 증가
    board.increaseViews();

    // 댓글도 함께 조회 (트리구조 + 페이징)
    Page<CommentResponseDto> comments = commentService.getComments(boardId, pageable);

    return BoardDetailResponseDto.from(board, comments);

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
