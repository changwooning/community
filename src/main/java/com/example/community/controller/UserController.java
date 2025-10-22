package com.example.community.controller;

import com.example.community.dto.user.MyPageResponseDto;
import com.example.community.dto.user.UserRequestDto;
import com.example.community.dto.user.UserResponseDto;
import com.example.community.jwt.JwtUserAuthentication;
import com.example.community.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController { // 지금 생각해 보니 동시에 회원가입을 시도할 수 있음..? 해결 방법은 ?

  private final UserService userService;

  /**
   * 회원가입
   */
  @PostMapping("/signup")
  public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
    UserResponseDto savedUser = userService.signup(userRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
  }

  /**
   * 마이페이지 조회 (JWT 기반)
   */
  @GetMapping("/mypage")
  public ResponseEntity<MyPageResponseDto> getMyPage(
      @AuthenticationPrincipal JwtUserAuthentication jwtUserAuthentication,
      @PageableDefault(size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

    Long userPk = jwtUserAuthentication.getUserPk(); // JWT 에서 PK 추출
    MyPageResponseDto response = userService.getMyPage(userPk, pageable);
    return ResponseEntity.ok(response);
  }


}
