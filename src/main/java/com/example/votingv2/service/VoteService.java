package com.example.votingv2.service;

import com.example.votingv2.dto.VoteRequest;
import com.example.votingv2.dto.VoteResponse;
import com.example.votingv2.entity.*;
import com.example.votingv2.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 투표 생성, 조회, 사용자 투표 제출 및 삭제를 담당하는 서비스
 */
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final VoteItemRepository voteItemRepository;
    private final VoteResultRepository voteResultRepository;

    /**
     * 투표 생성 처리
     */
    public VoteResponse createVote(VoteRequest request) {
        User admin = userRepository.findByUsername("admin1")
                .orElseThrow(() -> new IllegalArgumentException("관리자 계정이 존재하지 않습니다."));

        Vote vote = Vote.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .createdBy(admin)
                .createdAt(LocalDateTime.now())
                .isClosed(false)
                .startTime(request.getStartTime())  // 추가
                .build();

        Vote savedVote = voteRepository.save(vote);

        if (request.getItems() != null) {
            List<VoteItem> items = request.getItems().stream()
                    .map(itemReq -> VoteItem.builder()
                            .vote(savedVote)
                            .itemText(itemReq.getItemText())
                            .description(itemReq.getDescription())
                            .build())
                    .toList();
            voteItemRepository.saveAll(items);
        }

        return toResponse(savedVote);
    }

    /**
     * 사용자 투표 제출 처리
     */
    public void submitVote(Long voteId, VoteRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표 없음"));

        VoteItem selectedItem = voteItemRepository.findById(request.getSelectedItemId())
                .orElseThrow(() -> new IllegalArgumentException("선택한 항목 없음"));

        // 중복 투표 방지
        if (voteResultRepository.findByUserIdAndVoteId(user.getId(), voteId).isPresent()) {
            throw new IllegalStateException("이미 투표하셨습니다.");
        }

        VoteResult result = VoteResult.builder()
                .user(user)
                .vote(vote)
                .voteItem(selectedItem)
                .votedAt(LocalDateTime.now())
                .build();

        voteResultRepository.save(result);
    }

    /**
     * 투표 단건 조회 (항목 포함)
     */
    public VoteResponse getVoteById(Long id) {
        Vote vote = voteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 투표가 존재하지 않습니다."));

        List<VoteItem> items = voteItemRepository.findByVoteId(id);

        return VoteResponse.builder()
                .id(vote.getId())
                .title(vote.getTitle())
                .description(vote.getDescription())
                .deadline(vote.getDeadline())
                .isClosed(vote.isClosed())
                .createdAt(vote.getCreatedAt())
                .items(items.stream()
                        .map(item -> VoteResponse.Item.builder()
                                .itemId(item.getId())
                                .itemText(item.getItemText())
                                .itemDescription(item.getDescription())
                                .build())
                        .toList())
                .build();
    }

    /**
     * 전체 투표 목록 조회
     */
    public List<VoteResponse> getAllVotes() {
        return voteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 특정 투표 삭제 (항목과 결과까지 함께 삭제)
     */
    @Transactional
    public void deleteVote(Long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("해당 투표가 존재하지 않습니다."));

        // ✅ 1. 해당 투표의 항목 리스트 가져오기
        List<VoteItem> voteItems = voteItemRepository.findByVoteId(voteId);

        // ✅ 2. 각 항목에 대한 투표 결과 삭제
        for (VoteItem item : voteItems) {
            voteResultRepository.deleteAll(voteResultRepository.findByVoteItemId(item.getId()));
        }

        // ✅ 3. 항목 삭제
        voteItemRepository.deleteAll(voteItems);

        // ✅ 4. 마지막으로 투표 자체 삭제
        voteRepository.delete(vote);
    }


    /**
     * 내부 변환 로직: Vote 엔티티 → VoteResponse DTO
     */
    private VoteResponse toResponse(Vote vote) {
        List<VoteItem> items = voteItemRepository.findByVoteId(vote.getId());

        //  현재 시간이 마감일 이후면 true
        boolean isClosed = LocalDateTime.now().isAfter(vote.getDeadline());

        return VoteResponse.builder()
                .id(vote.getId())
                .title(vote.getTitle())
                .description(vote.getDescription())
                .deadline(vote.getDeadline())
                .isClosed(isClosed) //  여기서 실시간 계산된 값 사용
                .startTime(vote.getStartTime())  // 추가
                .createdAt(vote.getCreatedAt())
                .items(items.stream()
                        .map(item -> VoteResponse.Item.builder()
                                .itemId(item.getId())
                                .itemText(item.getItemText())
                                .itemDescription(item.getDescription())
                                .build())
                        .toList())
                .build();
    }
    public int countVotesByItem(Long voteId, Long itemId) {
        return voteResultRepository.countByVoteIdAndVoteItemId(voteId, itemId);
    }

}
