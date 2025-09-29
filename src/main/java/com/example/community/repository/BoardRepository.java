package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.entity.User;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

  // 유저가 작성한 게시글 (페이징)
  Page<Board> findByUser(User user, Pageable pageable);

  Page<Board> findAll(Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select b from Board b where b.id = :boardId")
  Optional<Board> findByIdWithLock(@Param("boardId") Long boardId);
}
