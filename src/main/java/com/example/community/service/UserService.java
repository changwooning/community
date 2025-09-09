package com.example.community.service;

import com.example.community.entity.User;
import com.example.community.enums.Role;
import com.example.community.repository.UserRepository;
import com.example.community.util.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public void signup(String userId, String password, String nickname) {
    // 아이디 중복검사
    if (userRepository.findByUserId(userId).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
    }

    // 닉네임 중복검사
    if (userRepository.findByNickname(nickname).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
    }

    if (!PasswordValidator.isValid(password)) {
      throw new IllegalArgumentException("비밀번호는 10자리 이상, 영문+숫자 조합이여야 합니다.");
    }

    User user = User.builder()
        .userId(userId)
        .password(password)
        .nickname(nickname)
        .role(Role.USER)
        .build();

    userRepository.save(user);

  }


}
