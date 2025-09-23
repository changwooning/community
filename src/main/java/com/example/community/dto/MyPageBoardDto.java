package com.example.community.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageBoardDto {

  private Long boardId;
  private String title;
  private LocalDateTime createdAt;
  private int views;
  private String message;


}
