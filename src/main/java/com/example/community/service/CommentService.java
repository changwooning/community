package com.example.community.service;

import com.example.community.dto.comment.CommentRequestDto;
import com.example.community.dto.comment.CommentResponseDto;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.CommentNotFoundException;
import com.example.community.exception.InvalidCommentException;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.CommentRepository;
import com.example.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final BoardRepository boardRepository;
  private final UserRepository userRepository;

  @Transactional
  public CommentResponseDto createdComment(Long boardId, Long userId,
      CommentRequestDto requestDto) {

    // 게시글 검증
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new BoardNotFoundException("해당 게시글이 존재하지 않습니다."));

    // 유저 검증
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

    // 부모 댓글 검증
    Comment parent = null;
    if (requestDto.getParentId() != null) {
      parent = commentRepository.findById(requestDto.getParentId())
          .orElseThrow(() -> new CommentNotFoundException("해당 상위 댓글은 존재하지 않습니다."));

      if (parent.getChildren().size() >= 10) {
        throw new InvalidCommentException("대댓글은 최대 10개까지만 작성 가능합니다.");
      }
    }

    // 댓글 생성
    Comment comment = Comment.builder()
        .content(requestDto.getContent())
        .user(user)
        .board(board)
        .parent(parent)
        .build();

    Comment saved = commentRepository.save(comment);

    return CommentResponseDto.builder()
        .id(saved.getId())
        .content(saved.getContent())
        .nickName(user.getNickName())
        .createdAt(saved.getCreatedAt())
        .parentId(parent != null ? parent.getId() : null)
        .build();
  }

  // 댓글 목록 조회
  @Transactional(readOnly = true)
  public Page<CommentResponseDto> getComments(Long boardId, Pageable pageable) {

    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new BoardNotFoundException("게시글을 찾을 수 없습니다."));

    // 루트 댓글만 페이징으로 가져옴
    Page<Comment> rootComments = commentRepository.findByBoardAndParentIsNull(board, pageable);

    // 각 루트 댓글 + children 트리 구조로 변환
    return rootComments.map(CommentResponseDto::from);
  }


}
