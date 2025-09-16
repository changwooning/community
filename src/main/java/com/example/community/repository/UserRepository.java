package com.example.community.repository;

import com.example.community.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserId(String userId); // 아이디 중복검사용
<<<<<<< HEAD
    Optional<User> findByNickName(String nickname); // 닉네임 중복검사용
=======
    Optional<User> findByNickname(String nickname); // 닉네임 중복검사용
>>>>>>> 8d77a13 (feat: 회원가입 기능 전체 구현)
}
