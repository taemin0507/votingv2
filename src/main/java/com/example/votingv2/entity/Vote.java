package com.example.votingv2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 투표 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "Vote")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 투표 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 설명

    private LocalDateTime deadline; // 마감일

    private boolean isClosed = false; // 마감 여부

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy; // 생성한 관리자 계정

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성 시각
}
