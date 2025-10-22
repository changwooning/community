package com.example.community.dto.auth;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseDto {

  private String accessToken;
  private String refreshToken;

  public static TokenResponseDto of(Map<String, String> tokens) {
    return TokenResponseDto.builder()
        .accessToken(tokens.get("accessToken"))
        .refreshToken(tokens.get("refreshToken"))
        .build();
  }


}
