package com.example.community.jwt;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Redis에 RefreshToken 저장, AccessToken 블랙리스트 관리하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

  private final RedisTemplate<String, Object> redisTemplate;

  // 로그아웃 시 AccessToken 블랙리스트 등록
  // TTL (Time To Live)은 토큰의 남은 만료 시간만큼 지정
  public void blacklistToken(String token, long expirationMillis) {
    redisTemplate.opsForValue().set(token, "BLACKLISTED", expirationMillis, TimeUnit.MILLISECONDS);
  }

  // AccessToken이 블랙리스트에 등록되었는지 확인
  public boolean isBlacklisted(String token) {
    return redisTemplate.hasKey(token);
  }

  // RefreshToken 저장 (PK 기준)
  public void saveRefreshToken(Long userPk, String refreshToken, long expirationMillis) {
    redisTemplate.opsForValue()
        .set("RT:" + userPk, refreshToken, expirationMillis, TimeUnit.MILLISECONDS);
  }

  // RefreshToken 조회
  public String getRefreshToken(Long userPk) {
    return (String) redisTemplate.opsForValue().get("RT:" + userPk);
  }

  // RefreshToken 삭제 (로그아웃 시)
  public void deleteRefreshToken(Long userPk){
    redisTemplate.delete("RT:" + userPk);
  }

}
