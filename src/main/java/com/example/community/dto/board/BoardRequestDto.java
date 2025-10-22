package com.example.community.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {

  @NotBlank(message = "제목은 필수 입력 값입니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입력 값입니다.")
  private String content;

}
