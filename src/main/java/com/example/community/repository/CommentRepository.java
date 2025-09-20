package com.example.community.repository;

import com.example.community.entity.Comment;
import com.example.community.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  // 유저가 작성한 댓글 (페이징)
  Page<Comment> findByUser(User user, Pageable pageable);

}
