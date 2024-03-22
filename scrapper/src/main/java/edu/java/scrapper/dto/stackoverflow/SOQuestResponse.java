package edu.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record SOQuestResponse(List<ItemResponse> items) {
    public record ItemResponse(
        @JsonProperty("question_id")
        Long id,
        @JsonProperty("link")
        String url,
        @JsonProperty("last_activity_date")
        OffsetDateTime lastUpdateTime) {
    }
}
