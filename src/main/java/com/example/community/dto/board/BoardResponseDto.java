package com.example.community.dto.board;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class BoardResponseDto {

  private Long id;
  private String title;
  private String content;
  private String nickName;
  private int views;
  private LocalDateTime createdAt;

}
