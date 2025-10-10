package com.example.community.controller;

import com.example.community.dto.BoardDetailResponseDto;
import com.example.community.dto.BoardListResponseDto;
import com.example.community.dto.BoardRequestDto;
import com.example.community.dto.BoardResponseDto;
import com.example.community.enums.SortType;
import com.example.community.service.BoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @PostMapping("/post")
  public ResponseEntity<BoardResponseDto> createdBoard(
      @Valid @RequestBody BoardRequestDto requestDto,
      HttpSession session) {

    Long userId = (Long) session.getAttribute("loginUser");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    BoardResponseDto response = boardService.createBoard(userId, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/listAll")
  public ResponseEntity<Page<BoardListResponseDto>> getBoardList(
      @PageableDefault(size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
      @RequestParam(defaultValue = "CREATED_AT") SortType sortBy) {

    return ResponseEntity.ok(boardService.getBoardList(pageable, sortBy));
  }

  @GetMapping("/{boardId}")
  public ResponseEntity<BoardDetailResponseDto> getBoardDetail(@PathVariable Long boardId,
      @PageableDefault(size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

    BoardDetailResponseDto response = boardService.getBoardDetail(boardId, pageable);

    return ResponseEntity.ok(response);

  }


}
