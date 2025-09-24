package com.example.community.controller;

import com.example.community.dto.BoardRequestDto;
import com.example.community.dto.BoardResponseDto;
import com.example.community.exception.InvalidBoardException;
import com.example.community.service.BoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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



}
