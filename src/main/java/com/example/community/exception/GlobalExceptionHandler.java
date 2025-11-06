package com.example.community.exception;


import com.example.community.dto.common.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
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

  // DTO 유효성 검증 실패 처리
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

  // 요청 본문이 비었거나 JSON 파싱 실패
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException e) {
    ErrorResponse response = ErrorResponse.builder()
        .code("INVALID_REQUEST")
        .message("요청 본문이 비었습니다.")
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  // JSON 직렬화/역직렬화 오류
  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<ErrorResponse> handleJsonProcession(JsonProcessingException e) {
    log.error("JSON parsing error", e);

    ErrorResponse response = ErrorResponse.builder()
        .code("JSON_PARSE_ERROR")
        .message("데이터 처리 중 오류가 발생했습니다.")
        .build();

    return ResponseEntity.internalServerError().body(response);
  }

  // Redis 연결 오류
  @ExceptionHandler(RedisConnectionFailureException.class)
  public ResponseEntity<ErrorResponse> handleRedis(RedisConnectionFailureException e) {
    log.error("Redis connection failed", e);

    ErrorResponse response = ErrorResponse.builder()
        .code("REDIS_UNAVAILABLE")
        .message("캐시 서버와 연결할 수 없습니다. 잠시 후 다시 시도해주세요.")
        .build();

    return ResponseEntity.status(503).body(response);
  }

  // 그 외 모든 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handlerException(Exception e) {

    log.error("Unexcepted error", e);
    ErrorResponse response = ErrorResponse.builder()
        .code("INTERNAL_SERVER_ERROR")
        .message("서버 오류가 발생했습니다.")
        .build();

    return ResponseEntity.internalServerError().body(response);
  }


}
