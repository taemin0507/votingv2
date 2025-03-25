package com.example.votingv2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자의 투표 결과를 저장하는 엔티티
 */
@Entity
@Table(name = "VoteResult", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "vote_id"})
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 투표한 사용자

    @ManyToOne
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote; // 어떤 투표인지

    @ManyToOne
    @JoinColumn(name = "vote_item_id", nullable = false)
    private VoteItem voteItem; // 선택한 항목

    private LocalDateTime votedAt = LocalDateTime.now(); // 투표 시각
}
