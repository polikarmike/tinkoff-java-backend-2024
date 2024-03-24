package edu.java.scrapper.dto.entity;

import java.net.URI;
import java.time.OffsetDateTime;

public interface LinkEntity {
    Long getId();

    URI getUri();

    OffsetDateTime getLastUpdatedAt();
}
