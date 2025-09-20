package com.example.community.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MyPageResponseDto {

  private String message; // 마이페이지 조회 성공 메시지
  private List<MyPageBoardDto> boards;
  private List<MyPageCommentDto> comments;

}
