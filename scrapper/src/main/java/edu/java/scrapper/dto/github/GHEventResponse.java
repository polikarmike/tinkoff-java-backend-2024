package edu.java.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GHEventResponse(
    String type,
    @JsonProperty("payload") Payload payload,
    @JsonProperty("created_at") OffsetDateTime createdAt) {
    public record Payload(
        @JsonProperty("issue") Issue issue,
        @JsonProperty("pull_request") PullRequest pullRequest

    ) {
        public record Issue(@JsonProperty("title") String title)  {}

        public record PullRequest(@JsonProperty("title") String title) {}
    }
}


