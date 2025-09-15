package com.example.community.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 아이디,닉네임,패스워드 등 예외 잡아서 API 응답(JSON) 변환

  // 아이디 중복 예외 처리
  // - 이미 존재 하는 userId 로 회원가입 요청 시 발생
  @ExceptionHandler(DuplicateUserIdException.class)
  public ResponseEntity<Map<String, String>> handleDuplicateUserId(DuplicateUserIdException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("error", e.getMessage()));  // 409 에러
  }

  // 닉네임 중복 예외 처리
  // - 이미 존재하는 nickName 으로 회원가입 요청 시 발생
  @ExceptionHandler(DuplicateNickNameException.class)
  public ResponseEntity<Map<String, String>> handleDuplicateNickName(DuplicateNickNameException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("error", e.getMessage()));  // 409 에러
  }

  // 비밀번호 정책 위반 예외 처리
  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<Map<String, String>> handleInvalidPassword(InvalidPasswordException e) {
    return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));  // 400에러
  }

  // Dto 유효성 검증 실패 처리
  // - @Valid 어노테이션 검증하다 실패 했을 때 발생
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    e.printStackTrace();
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
  }

  // 그 외 모든 예외 처리
  // - 위에서 처리못한 예외
  // - 500 에러
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleException(Exception e) {
    e.printStackTrace();
    Map<String, String> error = new HashMap<>();
    error.put("error", "서버 내부 오류가 발생했습니다.");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

  }

}
