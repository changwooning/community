package com.example.community.entity;

import com.example.community.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true,nullable = false, length = 50)
  private String userId;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(unique = true, nullable = false, length = 30)
  private String nickname;

  // STRING -> 가독성 + enum 추가 시 안전성
  @Enumerated(EnumType.STRING)  // db에 enum의 문자열 (USER/ADMIN) 저장
  @Column(nullable = false, length = 20)
  private Role role = Role.USER;

  @CreationTimestamp // insert 시점에 자동으로 현재 시간 저장
  @Column(nullable = false, updatable = false)
  private LocalDateTime created_At;

  @UpdateTimestamp  // update 시점에 자동으로 현재 시간 갱신
  @Column(nullable = false)
  private LocalDateTime updated_At;

  private LocalDateTime deleted_At; // 삭제용도 (굳이 이기도 하고,,)
}
