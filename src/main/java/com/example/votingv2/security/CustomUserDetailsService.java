package com.example.votingv2.security;

import com.example.votingv2.entity.User;
import com.example.votingv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * JWT 토큰에서 추출한 username으로 유저 정보를 DB에서 조회하는 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // username을 기준으로 UserDetails 객체 반환 (Spring Security 용도)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Spring Security용 UserDetails 객체 생성
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // 실제 인증은 이미 완료됐으므로 의미 없음
                .roles(user.getRole())        // 권한 부여
                .build();
    }
}
