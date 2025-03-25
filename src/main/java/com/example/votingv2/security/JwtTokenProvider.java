package com.example.votingv2.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰을 생성하고, 검증하고, 사용자 정보를 추출하는 클래스
 */
@Component
public class JwtTokenProvider {

    // application.properties에서 불러올 JWT 시크릿 키와 만료 시간
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    // Bean 초기화 시점에 Key 객체 생성
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username) // 토큰 주제: 사용자 ID
                .claim("role", role) // 사용자 역할 정보 포함
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expiry) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    // 토큰의 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 사용자 이름 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token)
                .getBody().getSubject();
    }

    // 토큰에서 역할(Role) 추출
    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token)
                .getBody().get("role", String.class);
    }
}
