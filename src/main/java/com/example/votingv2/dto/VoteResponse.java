// VoteResponse.java
package com.example.votingv2.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VoteResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private boolean isClosed;
    private LocalDateTime createdAt;
}
