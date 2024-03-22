package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.exception.DataBaseError;
import edu.java.scrapper.exception.MissingChatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import static edu.java.scrapper.domain.jooq.tables.Chat.CHAT;

@Repository
@RequiredArgsConstructor
@Qualifier("JOOQChatRepository")
public class JOOQChatRepository implements ChatRepository {

    private final DSLContext dslContext;

    @Override
    public Chat add(Chat chat) {
        dslContext.insertInto(CHAT)
            .set(CHAT.ID, chat.getId())
            .set(CHAT.CREATED_AT, chat.getCreatedAt())
            .execute();
        return getById(chat.getId()).orElseThrow(() -> new DataBaseError("Error creating chat"));
    }

    @Override
    public Chat remove(Long id) {
        Chat deletedChat = getById(id).orElseThrow(() -> new MissingChatException("Chat not found"));
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
