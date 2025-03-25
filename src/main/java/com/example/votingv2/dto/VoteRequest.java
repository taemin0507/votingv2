// VoteRequest.java
package com.example.votingv2.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
public class VoteRequest {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private List<VoteItemRequest> items;

    @Getter @Setter
    public static class VoteItemRequest {
        private String itemText;     // 항목 이름
        private String description;  // 항목 설명
    }
}


