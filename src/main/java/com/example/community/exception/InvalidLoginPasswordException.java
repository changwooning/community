package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class InvalidLoginPasswordException extends BusinessException{

  public InvalidLoginPasswordException(String message){
    super(message, HttpStatus.UNAUTHORIZED); // 401
  }
}
