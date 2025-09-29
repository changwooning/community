package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends BusinessException {

  public BoardNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
