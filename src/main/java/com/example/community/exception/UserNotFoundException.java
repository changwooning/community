package com.example.community.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException{

  public UserNotFoundException(String message){
    super(message, HttpStatus.NOT_FOUND); // 404
  }

}
