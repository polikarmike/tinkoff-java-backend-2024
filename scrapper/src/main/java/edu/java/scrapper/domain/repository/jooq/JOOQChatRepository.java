package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.exception.DataBaseError;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

@Repository
@RequiredArgsConstructor
public class JOOQChatRepository implements ChatRepository {

    private final DSLContext dslContext;
    private static final String CHAT_CREATION_ERROR_MESSAGE = "Chat was not created";

    @Override
    public Chat add(Chat chat) {
        dslContext.insertInto(CHAT)
            .set(CHAT.ID, chat.getId())
            .set(CHAT.CREATED_AT, chat.getCreatedAt())
            .execute();
        return getById(chat.getId()).orElseThrow(() -> new DataBaseError(CHAT_CREATION_ERROR_MESSAGE));
    }

    @Override
    public void remove(Long id) {
        dslContext.deleteFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .execute();
    }

    @Override
    public List<Chat> findAll() {
        return dslContext.selectFrom(CHAT)
            .fetchInto(Chat.class);
    }

    @Override
    public Optional<Chat> getById(Long id) {
        return dslContext.selectFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .fetchOptionalInto(Chat.class);
    }
}
