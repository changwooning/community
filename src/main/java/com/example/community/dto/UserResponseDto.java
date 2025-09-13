package com.example.community.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {

  private String userId;
  private String nickName;
  private LocalDateTime created_At;

}
