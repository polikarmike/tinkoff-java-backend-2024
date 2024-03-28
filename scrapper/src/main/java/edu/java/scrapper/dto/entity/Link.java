package edu.java.scrapper.dto.entity;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class Link {
    private Long id;
    private URI uri;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdatedAt;
}
