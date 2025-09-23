package com.example.community.controller;

import com.example.community.dto.LoginRequestDto;
import com.example.community.dto.LoginResponseDto;
import com.example.community.dto.MyPageResponseDto;
import com.example.community.dto.UserRequestDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.entity.User;
import com.example.community.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController { // 지금 생각해 보니 동시에 회원가입을 시도할 수 있음..? 해결 방법은 ?

  private final UserService userService;

  // requestDto, responseDto 로 분리하기 -> service 수정
  @PostMapping("/signup")
  public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
    UserResponseDto savedUser = userService.signup(userRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
      HttpSession session) {

    // service 에서 dto 반환
    LoginResponseDto response = userService.login(loginRequestDto);

    // 세션 저장
    session.setAttribute("loginUser", response.getId());

    // 응답 반환
    return ResponseEntity.ok(response);

  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpSession session) {
    session.invalidate(); // 세션 무효화
    return ResponseEntity.ok("로그아웃 성공");
  }

  @GetMapping("/mypage")
  public ResponseEntity<MyPageResponseDto> getMyPage(HttpSession session,
      @PageableDefault(size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

    Long userId = (Long) session.getAttribute("loginUser");

    MyPageResponseDto response = userService.getMyPage(userId, pageable);
    return ResponseEntity.ok(response);
  }


}
