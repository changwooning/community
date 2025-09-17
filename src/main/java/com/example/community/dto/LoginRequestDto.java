package com.example.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequestDto {

  @NotBlank(message = "아이디는 필수 입력 값입니다.")
  private String userId;

  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  @Size(min = 10, message = "비밀번호는 최소 10자리 이상, 영문+숫자 조합이여야 합니다.")
  private String password;


}
