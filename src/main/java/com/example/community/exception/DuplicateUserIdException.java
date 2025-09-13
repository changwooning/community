package com.example.community.exception;

// UserId 중복 예외
public class DuplicateUserIdException extends RuntimeException {

  public DuplicateUserIdException(String message) {
    super(message);
  }

}
