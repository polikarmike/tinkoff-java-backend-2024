package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.dto.entity.jooq_jdbc.Chat;
import edu.java.scrapper.exception.DataBaseError;
import edu.java.scrapper.exception.MissingChatException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

@Repository
@RequiredArgsConstructor
@Qualifier("JOOQChatRepository")
public class JOOQChatRepository implements ChatRepository {

    private final DSLContext dslContext;
    private static final String CHAT_CREATION_ERROR_MESSAGE = "Chat was not created";
    private static final String CHAT_NOT_FOUND_ERROR_MESSAGE = "Chat not found";

    @Override
    public Chat add(Chat chat) {
        dslContext.insertInto(CHAT)
            .set(CHAT.ID, chat.getId())
            .set(CHAT.CREATED_AT, chat.getCreatedAt())
            .execute();
        return getById(chat.getId()).orElseThrow(() -> new DataBaseError(CHAT_CREATION_ERROR_MESSAGE));
    }

    @Override
    public Chat remove(Long id) {
        Chat deletedChat = getById(id).orElseThrow(() -> new MissingChatException(CHAT_NOT_FOUND_ERROR_MESSAGE));
        dslContext.deleteFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .execute();
        return deletedChat;
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
