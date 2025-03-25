package com.example.votingv2.controller;

import com.example.votingv2.dto.LoginRequest;
import com.example.votingv2.dto.LoginResponse;
import com.example.votingv2.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 로그인 요청을 처리하는 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * 사용자 로그인 요청 처리
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        System.out.println(" 로그인 요청 수신: " + request.getUsername());
        return authService.login(request);
    }
}
