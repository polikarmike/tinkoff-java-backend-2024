package edu.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GHEventResponse(
    String type,
    Payload payload,
    @JsonProperty("created_at")
    OffsetDateTime createdAt) {
    public record Payload(
        Issue issue,
        @JsonProperty("pull_request")
        PullRequest pullRequest

    ) {
        public record Issue(String title)  {}

        public record PullRequest(String title) {}
    }
}


