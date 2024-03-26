package edu.java.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record SQQuestAnswerResponse(List<ItemResponse> items) {
    public record ItemResponse(
        @JsonProperty("last_activity_date")
        OffsetDateTime lastUpdateTime) {
    }
}
