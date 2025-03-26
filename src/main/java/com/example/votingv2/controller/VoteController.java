package com.example.votingv2.controller;

import com.example.votingv2.dto.VoteRequest;
import com.example.votingv2.dto.VoteResponse;
import com.example.votingv2.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 투표 API 컨트롤러
 */
@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    // 투표 생성
    @PostMapping
    public ResponseEntity<VoteResponse> createVote(@RequestBody VoteRequest request) {
        VoteResponse response = voteService.createVote(request);
        return ResponseEntity.ok(response);
    }

    // 투표 목록 조회
    @GetMapping
    public ResponseEntity<List<VoteResponse>> getAllVotes() {
        return ResponseEntity.ok(voteService.getAllVotes());
    }

    // 투표 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<VoteResponse> getVote(@PathVariable Long id) {
        return ResponseEntity.ok(voteService.getVoteById(id));
    }

    // ✅ 사용자 투표 제출
    @PostMapping("/{id}/vote")
    public ResponseEntity<String> submitVote(
            @PathVariable Long id,
            @RequestBody VoteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        voteService.submitVote(id, request, username);
        return ResponseEntity.ok("투표가 성공적으로 제출되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable Long id) {
        voteService.deleteVote(id);
        return ResponseEntity.noContent().build(); // 204 응답
    }

    @GetMapping("/{voteId}/items/{itemId}/count")
    public int getVoteItemCount(@PathVariable Long voteId,
                                @PathVariable Long itemId) {
        return voteService.countVotesByItem(voteId, itemId);
    }
}
