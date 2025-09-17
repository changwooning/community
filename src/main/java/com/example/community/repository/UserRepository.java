package com.example.community.repository;

import com.example.community.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserId(String userId); // 아이디 중복검사용
    Optional<User> findByNickName(String nickName); // 닉네임 중복검사용
}
