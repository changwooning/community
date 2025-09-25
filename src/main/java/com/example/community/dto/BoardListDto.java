package com.example.community.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardListDto {

  private Long id;
  private String title;
  private String nickName;
  private int views;
  private LocalDateTime createdAt;

}
