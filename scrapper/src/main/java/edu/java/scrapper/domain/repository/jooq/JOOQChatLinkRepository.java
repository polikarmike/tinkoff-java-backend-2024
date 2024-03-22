package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.domain.repository.ChatLinkRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;
import static edu.java.scrapper.domain.jooq.tables.LinkChat.LINK_CHAT;

@Repository
@RequiredArgsConstructor
@Qualifier("JOOQChatLinkRepository")
public class JOOQChatLinkRepository implements ChatLinkRepository {

    private final DSLContext dsl;

    @Override
    public int add(Long chatId, Long linkId) {
        return dsl.insertInto(LINK_CHAT, LINK_CHAT.LINK_ID, LINK_CHAT.CHAT_ID)
            .values(linkId, chatId)
            .execute();
    }

    @Override
    public int remove(Long chatId, Long linkId) {
        return dsl.deleteFrom(LINK_CHAT)
            .where(LINK_CHAT.LINK_ID.eq(linkId).and(LINK_CHAT.CHAT_ID.eq(chatId)))
            .execute();
    }

    @Override
    public List<Long> getLinkIdsByChatId(Long chatId) {
        return dsl.select(LINK_CHAT.LINK_ID)
            .from(LINK_CHAT)
            .where(LINK_CHAT.CHAT_ID.eq(chatId))
            .fetch()
            .map(record -> record.get(LINK_CHAT.LINK_ID));
    }

    @Override
    public List<Long> getChatIdsByLinkId(Long linkId) {
        return dsl.select(LINK_CHAT.CHAT_ID)
            .from(LINK_CHAT)
            .where(LINK_CHAT.LINK_ID.eq(linkId))
            .fetch()
            .map(record -> record.get(LINK_CHAT.CHAT_ID));
    }

    @Override
    public boolean exists(Long chatId, Long linkId) {
        Record1<Integer> result = dsl.selectCount()
            .from(LINK_CHAT)
            .where(LINK_CHAT.CHAT_ID.eq(chatId)
            .and(LINK_CHAT.LINK_ID.eq(linkId)))
            .fetchOne();
        return result != null && result.value1() > 0;
    }
}
