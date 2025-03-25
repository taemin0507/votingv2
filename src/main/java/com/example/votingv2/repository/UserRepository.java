package com.example.votingv2.repository;

import com.example.votingv2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 조회용 JPA 레포지토리
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // 로그인용
}
