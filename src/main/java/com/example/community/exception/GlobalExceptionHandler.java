package com.example.community.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 아이디,닉네임,패스워드 등 예외 잡아서 API 응답(JSON) 변환

  @ExceptionHandler(DuplicateUserIdException.class)
  public ResponseEntity<Map<String, String>> handleDuplicateUserId(DuplicateUserIdException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));  // 409 에러
  }

  @ExceptionHandler(DuplicateNickNameException.class)
  public ResponseEntity<Map<String, String>> handleDuplicateNickName(DuplicateNickNameException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));  // 409 에러
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<Map<String, String>> handleInvalidPassword(InvalidPasswordException e) {
    return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));  // 400에러
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String,String>> handleException(Exception e){
    return ResponseEntity.internalServerError().body(Map.of("error", "서버 내부 오류가 발생했습니다."));
  }

}
