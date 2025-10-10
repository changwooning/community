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

  // 최신순, 조회순 정렬은 pageable + SortType enum 기반
  Page<Board> findAll(Pageable pageable);

  // 비관적 락을 통한 동시성 제어 (조회 수 증가 시)
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select b from Board b where b.id = :boardId")
  Optional<Board> findByIdWithLock(@Param("boardId") Long boardId);

  // 특정 게시글 제목 검색 (인덱스 : idx_board_title 활용)
  Page<Board> findByTitleContaining(String keyword, Pageable pageable);

  // 특정 게시글 작성자 닉네임 검색 (인덱스 : idx_user_nickname 활용)
  Page<Board> findByUserNickName(String nickName, Pageable pageable);
}
