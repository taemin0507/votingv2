package com.example.votingv2.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 데이터를 담는 DTO
 */
@Getter @Setter
public class LoginRequest {
    private String username;  // 사용자 ID
    private String password;  // 사용자 비밀번호
}

