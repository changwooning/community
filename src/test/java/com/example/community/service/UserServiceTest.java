package com.example.community.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.community.dto.UserRequestDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.entity.User;
import com.example.community.enums.Role;
import com.example.community.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this); // Mock 초기화
  }

  @Test
  @DisplayName("회원가입 성공")
  void signup_success() {
    // given
    UserRequestDto request = new UserRequestDto("changwoo", "qwert12345", "changwooning");

    given(userRepository.findByUserId("changwoo1")).willReturn(Optional.empty());
    given(userRepository.findByNickname("changwooning")).willReturn(Optional.empty());

    User mockUser = User.builder()
        .id(1L)
        .userId(request.getUserId())
        .password(request.getPassword())
        .nickname(request.getNickname())
        .role(Role.USER)
        .build();

    given(userRepository.save(any(User.class))).willReturn(mockUser);

    // when
    UserResponseDto response = userService.signup(request);

    // then
    assertThat(response.getUserId()).isEqualTo("changwoo");
    assertThat(response.getNickname()).isEqualTo("changwooning");

  }

  @Test
  @DisplayName("아이디 중복 일 때 예외 발생")
  void signup_isDuplicateUserId() {
    // given
    UserRequestDto request = new UserRequestDto("changwoo", "qwert12345", "changwooning");

    given(userRepository.findByUserId("changwoo1"))
        .willReturn(Optional.of(new User()));

    // when & then
    assertThrows(IllegalArgumentException.class, () ->
        userService.signup(request));
  }

  @Test
  @DisplayName("비밀번호 검증 오류 시 예외 발생")
  void signup_invalidPassword() {
    // given
    UserRequestDto request = new UserRequestDto("chagnwoo", "qwert1234", "changwooning");

    // when & then
    assertThrows(IllegalArgumentException.class, () -> userService.signup(request));
  }
}