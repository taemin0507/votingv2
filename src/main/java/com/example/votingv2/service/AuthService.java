package com.example.votingv2.service;

import com.example.votingv2.dto.LoginRequest;
import com.example.votingv2.dto.LoginResponse;

/**
 * 로그인 처리를 위한 서비스 인터페이스
 */
public interface AuthService {
    LoginResponse login(LoginRequest request);  // 로그인 요청 처리
}
