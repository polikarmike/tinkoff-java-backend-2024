package edu.java.scrapper.dto.entity.jooq_jdbc;

import edu.java.scrapper.dto.entity.LinkEntity;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class Link implements LinkEntity {
    private Long id;
    private URI uri;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdatedAt;
}
