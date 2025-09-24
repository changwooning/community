package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class InvalidBoardException extends BusinessException {

  public InvalidBoardException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
