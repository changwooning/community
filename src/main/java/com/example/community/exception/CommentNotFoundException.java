package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends BusinessException {

  public CommentNotFoundException(String message) {

    super(message, HttpStatus.NOT_FOUND);
  }
}
