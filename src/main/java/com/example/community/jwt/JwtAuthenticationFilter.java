package com.example.community.jwt;

import com.example.community.entity.User;
import com.example.community.exception.BusinessException;
import com.example.community.exception.InvalidJwtTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT 필터 - 모든 요청 시 Authorization 헤더에서 JWT 추출 - 유효한 경우 SecurityContext에 저장
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final TokenBlacklistService tokenBlacklistService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {

      // Authorization 헤더 추출
      String header = request.getHeader("Authorization");

      if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;   // 토큰 없으면 인증 없이 통과
      }

      String token = header.substring(7);

      // 블랙리스트 확인
      if (tokenBlacklistService.isBlacklisted(token)) {
        throw new InvalidJwtTokenException("로그아웃된 토큰입니다.");
      }

      // 유효성 검증
      if (!jwtTokenProvider.validateToken(token)) {
        throw new InvalidJwtTokenException("유효하지 않은 JWT 토큰입니다.");
      }

      // 사용자 정보 추출
      Long userPk = jwtTokenProvider.getUserIdFromToken(token);

      // 인증 객체 생성 및 SecurityContext 저장
      JwtUserAuthentication authentication = new JwtUserAuthentication(userPk);
      SecurityContextHolder.getContext().setAuthentication(authentication);


    } catch (BusinessException e) {

      // 비즈니스 예외 발생 시 - 예를 들어 유효하지 않은 토큰
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
      return;
    }

    filterChain.doFilter(request, response);

  }


}
