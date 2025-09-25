package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

  // 유저가 작성한 게시글 (페이징)
  Page<Board> findByUser(User user, Pageable pageable);

  Page<Board> findAll(Pageable pageable);

}
