package com.example.community.service;

import com.example.community.dto.BoardListDto;
import com.example.community.dto.BoardListResponseDto;
import com.example.community.dto.BoardRequestDto;
import com.example.community.dto.BoardResponseDto;
import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.enums.SortType;
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

@Service
@RequiredArgsConstructor
public class BoardService {

  private final UserRepository userRepository;
  private final BoardRepository boardRepository;

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

  public BoardListResponseDto getBoardList(Pageable pageable, SortType sortBy) {

    PageRequest pageRequest = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(Direction.DESC, sortBy.getField())
    );

    Page<Board> boardPage = boardRepository.findAll(pageRequest);

    Page<BoardListDto> dtoPage = boardPage.map(board ->
        BoardListDto.builder()
            .id(board.getId())
            .title(board.getTitle())
            .nickName(board.getUser().getNickName())
            .views(board.getViews())
            .createdAt(board.getCreatedAt())
            .build()
    );

    return new BoardListResponseDto(dtoPage);

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
