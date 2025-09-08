package com.example.community.service;

import com.example.community.dto.UserRequestDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.entity.User;
import com.example.community.enums.Role;
import com.example.community.repository.UserRepository;
import com.example.community.util.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponseDto signup(UserRequestDto requestDto) {
    // 아이디 중복검사
    if (userRepository.findByUserId(requestDto.getUserId()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
    }

    // 닉네임 중복검사
    if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
    }

    if (!PasswordValidator.isValid(requestDto.getPassword())) {
      throw new IllegalArgumentException("비밀번호는 10자리 이상, 영문+숫자 조합이여야 합니다.");
    }

    User user = User.builder()
        .userId(requestDto.getUserId())
        .password(requestDto.getPassword())
        .nickname(requestDto.getPassword())
        .role(Role.USER)
        .build();

    User savedUser = userRepository.save(user);

    // User 엔티티 -> UserResponseDto 변환
    return new UserResponseDto(
        savedUser.getUserId(),
        savedUser.getNickname(),
        savedUser.getCreated_At()
    );

  }


}
