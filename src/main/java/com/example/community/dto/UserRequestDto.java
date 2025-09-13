package com.example.community.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

  @NotBlank(message = "아이디는 필수 입력 값입니다.")
  @Size(min = 4, max = 20,message = "아이디는 4자 이상 20자 이하로 입력해야 합니다.")
  private String userId;

  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  @Size(min = 10, message = "비밀번호는 최소 10자 이상이여야 합니다.")
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
      message = "비밀번호는 영문과 숫자를 조합해야 합니다.")
  private String password;

  @NotBlank(message = "닉네임은 필수 입력 값입니다.")
  @Size(max = 6, message = "닉네임은 최대 30자까지 가능합니다.")
  private String nickName;


}
