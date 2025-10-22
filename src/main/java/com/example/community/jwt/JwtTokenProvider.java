package com.example.community.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * JWT 생성, 검증을 담당하는 Provider 클래스
 * AccessToken / RefreshToken 모두 여기서 생성
 */

@Component
public class JwtTokenProvider {

  // JWT 암호화 키 (HS256 알고리즘)
  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  // AccessToken -> 30분 유호
  private final long ACCESS_TOKEN_EXP = 1000 * 60 * 30; // 30분
  // RefreshToken -> 7일 유효
  private final long REFRESH_TOKEN_EXP = 1000L * 60 * 60 * 24 * 7;  // 7일

  // AccessToken + RefreshToken 모두 발급해서 Map 형태로 반환
  public Map<String,String> generateTokens(Long userId){
    String accessToken = generateTokens(userId, ACCESS_TOKEN_EXP);
    String refreshToken = generateTokens(userId, REFRESH_TOKEN_EXP);

    return Map.of(
        "accessToken", accessToken,
        "refreshToken", refreshToken
    );
  }


  // 단일 토큰 생성
  private String generateTokens(Long userId, long expireTime){
    return Jwts.builder()
        .setSubject(String.valueOf(userId))              // 토큰 식별자 (유저 ID)
        .setIssuedAt(new Date())                        // 발급 시간
        .setExpiration(new Date(System.currentTimeMillis() + expireTime)) // 만료 시간
        .signWith(key)                    // 비밀 키로 서명
        .compact();                       // 토큰 문자열로 변환
  }


  // JWT 에서 userId(PK) 추출
  public Long getUserIdFromToken(String token){
    String subject = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();

    return Long.parseLong(subject);
  }

  // JWT 유효성 검증 (서명, 만료시간)
  public boolean validateToken(String token){
    try{
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    }catch(JwtException | IllegalArgumentException e){
      return false; // 잘못된 토큰이면 false
    }
  }

  // JWT 만료 시간 추출 (로그아웃 시 Redis TTL 지정용)
  public Date getExpiration(String token){
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();
  }

}
