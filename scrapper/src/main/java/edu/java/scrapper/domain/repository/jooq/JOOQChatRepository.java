package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.dto.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

@Repository
@RequiredArgsConstructor
public class JOOQChatRepository {

    private final DSLContext dsl;

    public void add(Chat chat) {
        dsl.insertInto(CHAT, CHAT.ID, CHAT.CREATED_AT)
            .values(chat.getId(), chat.getCreatedAt())
            .execute();
    }
}
