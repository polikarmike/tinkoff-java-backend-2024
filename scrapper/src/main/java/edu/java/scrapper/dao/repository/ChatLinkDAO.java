package edu.java.scrapper.dao.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ChatLinkDAO {

    private final JdbcTemplate jdbcTemplate;

    public void add(Long chatId, Long linkId) {
        jdbcTemplate.update("INSERT INTO Link_Chat (link_id, chat_id) VALUES (?, ?)",
            linkId, chatId);
    }

    public void remove(Long chatId, Long linkId) {
        jdbcTemplate.update("DELETE FROM Link_Chat WHERE chat_id = ? AND link_id = ?", chatId, linkId);
    }

    public List<Long> getLinkIdsByChatId(Long chatId) {
        return jdbcTemplate.queryForList("SELECT link_id FROM Link_Chat WHERE chat_id = ?", Long.class, chatId);
    }

    public List<Long> getChatIdsByLinkId(Long linkId) {
        return jdbcTemplate.queryForList("SELECT chat_id FROM Link_Chat WHERE link_id = ?", Long.class, linkId);
    }
}
