package com.example.community.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BoardDetailResponseDto {

  private Long id;
  private String title;
  private String content;
  private String nickName;
  private int views;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // 댓글 계층 구조 추가
  private Page<CommentResponseDto> comments;

}
