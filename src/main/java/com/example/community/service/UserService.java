package com.example.community.service;

import com.example.community.dto.LoginRequestDto;
import com.example.community.dto.LoginResponseDto;
import com.example.community.dto.MyPageBoardDto;
import com.example.community.dto.MyPageCommentDto;
import com.example.community.dto.MyPageResponseDto;
import com.example.community.dto.UserRequestDto;
import com.example.community.dto.UserResponseDto;
import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.entity.User;
import com.example.community.enums.Role;
import com.example.community.exception.DuplicateNickNameException;
import com.example.community.exception.DuplicateUserIdException;
import com.example.community.exception.InvalidLoginPasswordException;
import com.example.community.exception.InvalidPasswordException;
import com.example.community.exception.UnauthorizedAccessException;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.CommentRepository;
import com.example.community.repository.UserRepository;
import com.example.community.util.PasswordValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BoardRepository boardRepository;
  private final CommentRepository commentRepository;

  // 회원가입
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
    return UserResponseDto.builder()
        .userId(savedUser.getUserId())
        .nickName(savedUser.getNickName())
        .role(savedUser.getRole())
        .created_At(savedUser.getCreated_At())
        .message("회원가입 성공!")
        .build();

  }

  // 로그인
  public LoginResponseDto login(LoginRequestDto requestDto) {

    User user = userRepository.findByUserId(requestDto.getUserId())
        .orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디입니다."));

    if (!user.getPassword().equals(requestDto.getPassword())) {
      throw new InvalidLoginPasswordException("비밀번호가 일치하지 않습니다.");
    }

    return LoginResponseDto.builder()
        .id(user.getId())
        .userId(user.getUserId())
        .nickName(user.getNickName())
        .role(user.getRole())
        .message("로그인 성공!")
        .build();

  }

  // 마이페이지
  public MyPageResponseDto getMyPage(Long userId, Pageable pageable) {

    // 로그인 여부 검증하고
    validateLoginUser(userId);

    // 유저 존재 여부 검증 -> db와 세션 불일치 가능성 있기 때문
    User user = findUserById(userId);

    Page<Board> boardPage = boardRepository.findByUser(user, pageable);
    List<MyPageBoardDto> boardDtos = boardPage.stream()
        .map(board -> MyPageBoardDto.builder()
            .boardId(board.getId())
            .title(board.getTitle())
            .views(board.getViews())
            .created_At(board.getCreatedAt())
            .build())
        .toList();

    Page<Comment> commentPage = commentRepository.findByUser(user, pageable);
    List<MyPageCommentDto> commentDtos = commentPage.stream()
        .map(comment -> MyPageCommentDto.builder()
            .commentId(comment.getId())
            .boardTitle(comment.getBoard().getTitle())
            .content(comment.getContent())
            .created_At(comment.getCreatedAt())
            .build())
        .toList();

    String message =
        (boardDtos.isEmpty() && commentDtos.isEmpty()) ? "작성한 글이나 댓글이 없습니다." : "마이페이지 조회 성공";

    return MyPageResponseDto.builder()
        .message(message)
        .boards(boardDtos)
        .comments(commentDtos)
        .build();
  }

  // 로그인 여부 확인
  private void validateLoginUser(Long userId) {
    if (userId == null) {
      throw new UnauthorizedAccessException("로그인이 필요한 서비스 입니다.");
    }
  }

  // 유저 존재 여부 확인
  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("마이페이지 조회 실패 : 해당 유저가 존재하지 않습니다."));
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
