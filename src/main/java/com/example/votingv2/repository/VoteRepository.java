package com.example.votingv2.repository;

import com.example.votingv2.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 투표 관련 레포지토리
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
