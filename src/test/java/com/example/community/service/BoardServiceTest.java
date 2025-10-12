package com.example.community.service;

import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.UserRepository;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("docker")
class BoardServiceTest {

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private UserRepository userRepository;

  private static final int DUMMY_COUNT = 80_000;
  private static final String KEYWORD = "테스트 제목 : 6";

  @BeforeAll
  @Transactional
  void setupDummyData() {

    boardRepository.deleteAll();
    userRepository.deleteAll();

    User user = userRepository.save(User.builder()
        .userId("testuser")
        .password("password1234")
        .nickName("tester")
        .build()
    );

    long existing = boardRepository.count();
    if (existing >= DUMMY_COUNT) {
      System.out.println("이미 더미 데이터 존재 (" + existing + "건)");
      return;
    }

    IntStream.rangeClosed(1, DUMMY_COUNT).forEach(i -> {
      Board board = Board.builder()
          .title("테스트 제목 : " + i)
          .content("테스트 내용입니다 " + i)
          .views(0)
          .user(user)
          .build();
      boardRepository.save(board);

      if (i % 10_000 == 0) {
        System.out.println(i + "건 저장 완료...");
      }
    });

    System.out.println("총 " + DUMMY_COUNT + "건 더미 데이터 생성 완료!");

  }

  private void runPerformanceTest(String label, Runnable testLogic) {
    long start = System.currentTimeMillis();
    testLogic.run();
    long end = System.currentTimeMillis();
    System.out.println("\n=== [" + label + "] 성능 테스트 ===");
    System.out.println("실행 시간(ms): " + (end - start));
  }

  // 인덱스 없이 테스트
  @Test
  @Transactional(readOnly = true)
  void testContaining() {
    PageRequest pageable = PageRequest.of(0, 10);
    runPerformanceTest("LIKE '%keyword%' (Containing)", () -> {
      Page<Board> results = boardRepository.findByTitleContaining(KEYWORD, pageable);
      System.out.println("조회된 건수: " + results.getTotalElements());
    });
  }

  // 인덱스 있이 테스트
  @Test
  @Transactional(readOnly = true)
  void testStartingWith() {

    PageRequest pageable = PageRequest.of(0, 10);
    runPerformanceTest("LIKE '테스트 제목 : 6%' (StartingWith)", () -> {
      Page<Board> results = boardRepository.findByTitleStartingWith(KEYWORD, pageable);
      System.out.println("조회된 건수: " + results.getTotalElements());
    });

  }

  // 조회수 정렬 인덱스 테스트
  @Test
  @Transactional(readOnly = true)
  void testOrderByViews() {
    PageRequest pageable = PageRequest.of(0, 10);

    runPerformanceTest("ORDER BY views DESC (조회수 내림차순 정렬)", () -> {
      Page<Board> results = boardRepository.findAllByOrderByViewsDesc(pageable);
      System.out.println("조회된 건수 : " + results.getTotalElements());

    });
  }

  // 조회수 증가 로직 성능 테스트
  @Test
  @Transactional
  void testIncreaseViewsPerformance(){
    runPerformanceTest("UPDATE views (조회수 증가 10,000회)", () -> {
      IntStream.rangeClosed(1,10_000).forEach(i -> {
        boardRepository.findById((long) i).ifPresent(Board::increaseViews);
      });
    });
  }


}