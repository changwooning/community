package com.example.community.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

  private String code;  // 예외 코드
  private String message; // 사용자 메시지

}
