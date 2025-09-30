package com.example.community.repository;

import com.example.community.dto.CommentResponseDto;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  // 유저가 작성한 댓글 (페이징)
  Page<Comment> findByUser(User user, Pageable pageable);

  Page<Comment> findByBoard(Board board, Pageable pageable);

  Page<Comment> findByBoardAndParentIsNull(Board board, Pageable pageable);

}
