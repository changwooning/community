package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends BusinessException {

  public InvalidRefreshTokenException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
