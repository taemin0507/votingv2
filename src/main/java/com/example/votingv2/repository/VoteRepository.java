package com.example.votingv2.repository;

import com.example.votingv2.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 투표 관련 레포지토리
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findAllByIsDeletedTrue();
    Optional<Vote> findByIdAndIsDeletedTrue(Long id);
}
