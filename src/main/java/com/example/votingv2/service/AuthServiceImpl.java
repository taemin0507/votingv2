package com.example.votingv2.service;

import com.example.votingv2.dto.LoginRequest;
import com.example.votingv2.dto.LoginResponse;
import com.example.votingv2.entity.User;
import com.example.votingv2.repository.UserRepository;
import com.example.votingv2.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 로그인 로직 구현 클래스
 * 1. 사용자 존재 여부 확인
 * 2. 비밀번호 검증
 * 3. JWT 토큰 생성 및 응답
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;          // 사용자 조회용
    private final PasswordEncoder passwordEncoder;        // 비밀번호 암호화 비교용
    private final JwtTokenProvider jwtTokenProvider;      // JWT 생성기

    @Override
    public LoginResponse login(LoginRequest request) {
        System.out.println("✅ AuthServiceImpl.login() 호출됨");
        // 1. 사용자 조회
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 평문 비밀번호 비교 (임시)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
/*        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }*/

        // 3. JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());

        // 4. 응답 객체 반환
        return new LoginResponse(token, user.getUsername(), user.getRole());
    }
}
