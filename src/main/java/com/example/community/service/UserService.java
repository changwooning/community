package com.example.community.service;

import com.example.community.dto.auth.LoginRequestDto;
import com.example.community.dto.user.MyPageBoardDto;
import com.example.community.dto.user.MyPageCommentDto;
import com.example.community.dto.user.MyPageResponseDto;
import com.example.community.dto.user.UserRequestDto;
import com.example.community.dto.user.UserResponseDto;
import com.example.community.entity.User;
import com.example.community.enums.Role;
import com.example.community.exception.DuplicateNickNameException;
import com.example.community.exception.DuplicateUserIdException;
import com.example.community.exception.InvalidLoginPasswordException;
import com.example.community.exception.InvalidPasswordException;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.CommentRepository;
import com.example.community.repository.UserRepository;
import com.example.community.util.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BoardRepository boardRepository;
  private final CommentRepository commentRepository;
  private final PasswordEncoder passwordEncoder;

  // 회원가입
  public UserResponseDto signup(UserRequestDto requestDto) {

    validateUserId(requestDto.getUserId());
    validateNickName(requestDto.getNickName());
    validatePassword(requestDto.getPassword());

    String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

    User user = User.builder()
        .userId(requestDto.getUserId())
        .password(encodedPassword)
        .nickName(requestDto.getNickName())
        .role(Role.USER)
        .build();

    User savedUser = userRepository.save(user);

    // User 엔티티 -> UserResponseDto 변환
    return UserResponseDto.builder()
        .userId(savedUser.getUserId())
        .nickName(savedUser.getNickName())
        .role(savedUser.getRole())
        .createdAt(savedUser.getCreatedAt())
        .message("회원가입 성공!")
        .build();

  }

  // 로그인 검증 로직 (Jwt 발급용)
  // - 실제 토큰 발급은 AuthController 가 담당
  // - 여기서는 아이디/ 비밀번호 일치 여부만 확인
  public User validateLogin(LoginRequestDto requestDto) {
    // DB 에서 userId 로 유저 조회
    User user = userRepository.findByUserId(requestDto.getUserId())
        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디입니다."));

    // 입력한 비밀번호와 저장된 비밀번호 비교
    if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
      throw new InvalidLoginPasswordException("비밀번호가 일치하지 않습니다.");
    }

    // Controller 에서 JWT 발급 시 user.getId() (PK)로 처리하기 위해 반환
    return user;
  }

  // 마이페이지
  public MyPageResponseDto getMyPage(Long userPk, Pageable pageable) {

    // 유저 존재 여부 검증 -> db와 세션 불일치 가능성 있기 때문
    User user = userRepository.findById(userPk)
        .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

    Page<MyPageBoardDto> boards = boardRepository.findByUser(user, pageable)
        .map(board -> MyPageBoardDto.builder()
            .boardId(board.getId())
            .title(board.getTitle())
            .views(board.getViews())
            .createdAt(board.getCreatedAt())
            .build()
        );

    Page<MyPageCommentDto> comments = commentRepository.findByUser(user, pageable)
        .map(comment -> MyPageCommentDto.builder()
            .commentId(comment.getId())
            .boardTitle(comment.getBoard().getTitle())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .build()
        );

    return MyPageResponseDto.builder()
        .boards(boards)
        .comments(comments)
        .build();
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
