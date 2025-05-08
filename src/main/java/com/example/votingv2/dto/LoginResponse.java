package com.example.votingv2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 성공 시 클라이언트에 전달되는 응답 DTO
 */
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;  // 발급된 JWT 토큰
    private String username;     // 로그인한 사용자 ID
    private String role;         // 사용자 권한 (USER, ADMIN,de)
}
