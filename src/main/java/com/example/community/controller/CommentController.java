package com.example.community.controller;

import com.example.community.dto.comment.CommentRequestDto;
import com.example.community.dto.comment.CommentResponseDto;
import com.example.community.exception.UnauthorizedAccessException;
import com.example.community.service.CommentService;
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
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/boards/{boardId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  // 댓글 작성
  @PostMapping
  public ResponseEntity<CommentResponseDto> createdComment(@PathVariable Long boardId,
      @Valid @RequestBody CommentRequestDto requestDto,
      HttpSession session) {

    Long userId = (Long) session.getAttribute("loginUser");
    if (userId == null) {
      throw new UnauthorizedAccessException("로그인이 필요한 서비스 입니다.");
    }

    CommentResponseDto response = commentService.createdComment(boardId, userId, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);

  }

  // 댓글 조회
  @GetMapping
  public ResponseEntity<Page<CommentResponseDto>> getComments(
      @PathVariable Long boardId,
      @PageableDefault(size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

    Page<CommentResponseDto> response = commentService.getComments(boardId, pageable);
    return ResponseEntity.ok(response);
  }

}
