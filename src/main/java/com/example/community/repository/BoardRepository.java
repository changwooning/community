package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

  // 유저가 작성한 게시글 (페이징)
  Page<Board> findByUser(User user, Pageable pageable);

  // 최신순, 조회순 정렬은 pageable + SortType enum 기반
  Page<Board> findAll(Pageable pageable);

  // 특정 게시글 작성자 닉네임 검색 (인덱스 : idx_user_nickname 활용)
  Page<Board> findByUserNickName(String nickName, Pageable pageable);

  // 1. LIKE '%keyword%'
  Page<Board> findByTitleContaining(String keyword, Pageable pageable);

  // 2. LIKE 'keyword%'
  Page<Board> findByTitleStartingWith(String keyword, Pageable pageable);

  Page<Board> findAllByOrderByViewsDesc(Pageable pageable);

  /**
   * Redis -> DB 배치 반영을 위한 조회수 업데이트
   * Redis에 누적된 views 값을 DB에 반영
   * viewSyncService 에서 1분 주기 실행
   */
  @Modifying
  @Query("UPDATE Board b SET b.views = :views WHERE b.id = :boardId")
  void updateViews(@Param("boardId") Long boardId , @Param("views") int views);

}
