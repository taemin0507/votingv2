package com.example.votingv2.service;

import com.example.votingv2.dto.VoteRequest;
import com.example.votingv2.dto.VoteResponse;
import com.example.votingv2.entity.User;
import com.example.votingv2.entity.Vote;
import com.example.votingv2.entity.VoteItem;
import com.example.votingv2.repository.UserRepository;
import com.example.votingv2.repository.VoteItemRepository;
import com.example.votingv2.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final VoteItemRepository voteItemRepository;



    public List<VoteResponse> getAllVotes() {
        List<Vote> votes = voteRepository.findAll(); // 모든 투표 조회

        return votes.stream()
                .map(vote -> VoteResponse.builder()
                        .id(vote.getId())
                        .title(vote.getTitle())
                        .description(vote.getDescription())
                        .deadline(vote.getDeadline())
                        .isClosed(vote.isClosed())
                        .createdAt(vote.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public VoteResponse createVote(VoteRequest request) {
        // 테스트용: 사용자 admin1을 자동으로 찾아 연결
        User admin = userRepository.findByUsername("admin1")
                .orElseThrow(() -> new IllegalArgumentException("관리자 계정이 존재하지 않습니다."));

        Vote vote = Vote.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .createdAt(LocalDateTime.now())
                .createdBy(admin)
                .isClosed(false)
                .build();

        Vote saved = voteRepository.save(vote);

        // ✅ 항목들 저장
        if (request.getItems() != null) {
            List<VoteItem> items = request.getItems().stream()
                    .map(itemReq -> VoteItem.builder()
                            .vote(saved)
                            .itemText(itemReq.getItemText())
                            .description(itemReq.getDescription())
                            .build())
                    .toList();

            // 투표 항목 저장 (cascade 설정 없이 별도로 저장해야 한다면 여기에 repository.saveAll 호출 필요)
            voteItemRepository.saveAll(items);
        }

        return VoteResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .deadline(saved.getDeadline())
                .isClosed(saved.isClosed())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public VoteResponse getVoteById(Long id) {
        Vote vote = voteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 투표가 존재하지 않습니다."));

        return VoteResponse.builder()
                .id(vote.getId())
                .title(vote.getTitle())
                .description(vote.getDescription())
                .deadline(vote.getDeadline())
                .isClosed(vote.isClosed())
                .createdAt(vote.getCreatedAt())
                .build();
    }

}
