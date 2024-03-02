package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GHRepoResponse(
    @JsonProperty("name")
    String repoName,

    @JsonProperty("html_url")
    String link,

    @JsonProperty("created_at")
    OffsetDateTime createdAt,

    @JsonProperty("pushed_at")
    OffsetDateTime pushedAt,

    @JsonProperty("updated_at")
    OffsetDateTime updateAt) {

}
