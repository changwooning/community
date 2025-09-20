package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends BusinessException {

  public UnauthorizedAccessException(String message) {

    super(message, HttpStatus.UNAUTHORIZED);
  }
}
