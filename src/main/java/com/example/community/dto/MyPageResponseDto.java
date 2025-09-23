package com.example.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MyPageResponseDto {

  private Page<MyPageBoardDto> boards;
  private Page<MyPageCommentDto> comments;

}
