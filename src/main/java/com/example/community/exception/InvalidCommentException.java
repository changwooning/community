package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class InvalidCommentException extends BusinessException {

  public InvalidCommentException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
