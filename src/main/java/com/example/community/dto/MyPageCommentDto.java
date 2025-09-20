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
public class MyPageCommentDto {

  private Long commentId;
  private String boardTitle;
  private String content;
  private LocalDateTime created_At;
  private String message;


}
