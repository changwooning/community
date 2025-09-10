package com.example.community.controller;

import com.example.community.dto.UserRequestDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto) {
    UserResponseDto savedUser = userService.signup(userRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
  }


}
