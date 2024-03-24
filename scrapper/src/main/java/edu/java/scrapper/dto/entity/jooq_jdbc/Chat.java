package edu.java.scrapper.dto.entity.jooq_jdbc;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class Chat {
    private Long id;
    private OffsetDateTime createdAt;
}
