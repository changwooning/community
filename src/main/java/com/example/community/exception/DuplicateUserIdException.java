package com.example.community.exception;

import org.springframework.http.HttpStatus;

// UserId 중복 예외
public class DuplicateUserIdException extends BusinessException {

  public DuplicateUserIdException(String message) {
    super(message, HttpStatus.CONFLICT); // 409
  }

}
