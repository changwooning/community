package com.example.community.exception;


import com.example.community.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 비즈니스 로직 예외 처리 (공통)
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    ErrorResponse response = ErrorResponse.builder()
        .code(e.getClass().getSimpleName()) // 예외 클래스 이름을 코드로 사용
        .message(e.getMessage())
        .build();

    return ResponseEntity.status(e.getStatus()).body(response);
  }

  // Dto 유효성 검증 실패 처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {
    // 첫 번째 에러 메시지
    String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    ErrorResponse response = ErrorResponse.builder()
        .code("VALIDATION_ERROR")
        .message(errorMessage)
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e){
    ErrorResponse response = ErrorResponse.builder()
        .code("INVALID_REQUEST")
        .message("요청 본문이 비었습니다.")
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  // 그 외 모든 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handlerException(Exception e) {
    e.printStackTrace();
    ErrorResponse response = ErrorResponse.builder()
        .code("INTERNAL_SERVER_ERROR")
        .message(e.getMessage())
        .build();

    return ResponseEntity.internalServerError().body(response);
  }


}
