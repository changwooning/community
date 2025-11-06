package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class CacheDeserializeException extends  BusinessException {

  public CacheDeserializeException(String message) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
