package com.example.community.controller;

import com.example.community.dto.auth.LoginRequestDto;
import com.example.community.dto.auth.TokenResponseDto;
import com.example.community.entity.User;
import com.example.community.exception.InvalidRefreshTokenException;
import com.example.community.jwt.JwtTokenProvider;
import com.example.community.jwt.TokenBlacklistService;
import com.example.community.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 API (로그인 / 로그아웃 / 토큰 재발급)
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final TokenBlacklistService tokenBlacklistService;

  /**
   * 로그인 API 1. UserService 에서 로그인 검증 2. JWT AccessToken + RefreshToken 발급 3. RefreshToken은 Redis에
   * 저장
   */
  @PostMapping("/login")
  public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {

    // 로그인 검증 후 User 반환
    User user = userService.validateLogin(requestDto);

    // PK 기반 JWT 발급
    Map<String, String> tokens = jwtTokenProvider.generateTokens(user.getId());

    // RefreshToken Redis 에 저장
    tokenBlacklistService.saveRefreshToken(
        user.getId(),
        tokens.get("refreshToken"),
        1000L * 60 * 60 * 24 * 7   // 7일
    );

    // 응답 (AccessToken + RefreshToken)
    return ResponseEntity.ok(TokenResponseDto.of(tokens));

  }

  /**
   * 로그아웃 API AccessToken은 블랙리스트에 등록 RefreshToken은 Redis 에서 제거
   */
  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken) {
    String token = bearerToken.substring(7);  // "Bearer " 제거
    long expirationMillis =
        jwtTokenProvider.getExpiration(token).getTime() - System.currentTimeMillis();
    long userPk = jwtTokenProvider.getUserIdFromToken(token);

    // AccessToken 블랙리스트 등록
    tokenBlacklistService.blacklistToken(token, expirationMillis);
    // RefreshToken 삭제
    tokenBlacklistService.deleteRefreshToken(userPk);

    return ResponseEntity.ok("로그아웃 완료 (토큰 블랙리스트 등록)");
  }

  /**
   * AccessToken 재발급 API RefreshToken이 Redis에 유효하게 남아있으면 재발급
   */
  @PostMapping("/refresh")
  public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody Map<String, String> body) {

    Long userPk = Long.parseLong(body.get("userId"));
    String oldRefreshToken = body.get("refreshToken");
    String savedRefreshToken = tokenBlacklistService.getRefreshToken(userPk);

    // Redis에 RefreshToken이 존재하지 않거나 불일치하면 예외 발생
    if (savedRefreshToken == null || savedRefreshToken.equals(oldRefreshToken)) {
      throw new InvalidRefreshTokenException("유효하지 않은 RefreshToken 입니다.");
    }

    Map<String, String> newTokens = jwtTokenProvider.generateTokens(userPk);

    tokenBlacklistService.saveRefreshToken(
        userPk,
        newTokens.get("refreshToken"),
        1000L * 60 * 60 * 24 * 7);
    return ResponseEntity.ok(TokenResponseDto.of(newTokens));

  }

}
