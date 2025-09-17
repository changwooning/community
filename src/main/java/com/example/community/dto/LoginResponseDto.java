package com.example.community.dto;

import com.example.community.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {

  private Long id;
  private String userId;
  private String nickName;
  private Role role;
  private String message;

}
