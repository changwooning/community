package com.example.community.service;

import com.example.community.dto.UserRequestDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.entity.User;
import com.example.community.enums.Role;
import com.example.community.exception.DuplicateNickNameException;
import com.example.community.exception.DuplicateUserIdException;
import com.example.community.exception.InvalidPasswordException;
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

    validateUserId(requestDto.getUserId());
    validateNickName(requestDto.getNickName());
    validatePassword(requestDto.getPassword());

    User user = User.builder()
        .userId(requestDto.getUserId())
        .password(requestDto.getPassword())
        .nickName(requestDto.getNickName())
        .role(Role.USER)
        .build();

    User savedUser = userRepository.save(user);

    // User 엔티티 -> UserResponseDto 변환
    return new UserResponseDto(
        savedUser.getUserId(),
        savedUser.getNickName(),
        savedUser.getCreated_At()
    );

  }

  // 아이디 중복 예외 메서드 분리
  private void validateUserId(String userId) {
    if (userRepository.findByUserId(userId).isPresent()) {
      throw new DuplicateUserIdException("이미 존재하는 아이디입니다.");
    }
  }

  // 닉네임 중복 예외 메서드 분리
  private void validateNickName(String nickName) {
    if (userRepository.findByNickName(nickName).isPresent()) {
      throw new DuplicateNickNameException("이미 존재하는 닉네임입니다.");
    }
  }

  // 비밀번호 예외 메서드 분리
  private void validatePassword(String password) {
    if (!PasswordValidator.isValid(password)) {
      throw new InvalidPasswordException("비밀번호는 10자리 이상, 영문+숫자 조합이여야 합니다.");
    }
  }


}
