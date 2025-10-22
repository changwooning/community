package com.example.community.dto.user;

import com.example.community.enums.Role;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

  private String userId;
  private String nickName;
  private Role role;
  private LocalDateTime createdAt;
  private String message;

}
