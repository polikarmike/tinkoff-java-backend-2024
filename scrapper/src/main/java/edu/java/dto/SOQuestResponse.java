package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record SOQuestResponse(
    @JsonProperty("question_id")
    String questId,
    @JsonProperty("link")
    String url,
    String title,
    @JsonProperty("creation_date")
    OffsetDateTime creationDate,
    @JsonProperty("last_activity_date")
    OffsetDateTime lastActivityDate) {

}

