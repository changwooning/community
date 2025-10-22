package com.example.community.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtUserAuthentication extends AbstractAuthenticationToken {

  private final Long userPk;

  // JWT로 인증 성공 시 userId 만으로 인증 객체 생성
  public JwtUserAuthentication(Long userPk){
    super(null);
    this.userPk = userPk;
    setAuthenticated(true); // 인증 완료 상태로 설정
  }

  @Override
  public Object getCredentials(){
    return null; // 비밀번호 같은 인증 자격 정보는 JWT 인증에서는 불필요
  }

  @Override
  public Object getPrincipal(){
    return userPk;   // 현재 인증된 사용자 정보 (userId)
  }

  public Long getUserPk(){
    return userPk;
  }
}
