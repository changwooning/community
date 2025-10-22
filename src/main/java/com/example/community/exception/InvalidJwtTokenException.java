package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class InvalidJwtTokenException extends BusinessException {

  public InvalidJwtTokenException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
