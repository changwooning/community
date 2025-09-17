package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class DuplicateNickNameException extends BusinessException {

  public DuplicateNickNameException(String message) {
    super(message, HttpStatus.CONFLICT); // 409
  }

}
