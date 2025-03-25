package com.example.votingv2.controller;

import com.example.votingv2.dto.VoteRequest;
import com.example.votingv2.dto.VoteResponse;
import com.example.votingv2.entity.Vote;
import com.example.votingv2.repository.VoteRepository;
import com.example.votingv2.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 투표 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final VoteRepository voteRepository;

    @PostMapping
    public ResponseEntity<VoteResponse> createVote(@RequestBody VoteRequest request) {
        VoteResponse response = voteService.createVote(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 투표 목록 페이지를 보여주는 메서드
     */
    // ✅ 투표 목록을 JSON으로 반환 (프론트가 이걸 호출)
    @GetMapping
    public ResponseEntity<List<VoteResponse>> getAllVotes() {
        List<VoteResponse> votes = voteService.getAllVotes();
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoteResponse> getVoteById(@PathVariable Long id) {
        VoteResponse vote = voteService.getVoteById(id);
        return ResponseEntity.ok(vote);
    }

}
