package com.example.votingv2.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 투표 항목(후보자 등)을 저장하는 엔티티
 */
@Entity
@Table(name = "VoteItem")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote; // 속한 투표

    private String itemText; // 항목 이름

    @Column(columnDefinition = "TEXT")
    private String description; // 항목 설명
}
