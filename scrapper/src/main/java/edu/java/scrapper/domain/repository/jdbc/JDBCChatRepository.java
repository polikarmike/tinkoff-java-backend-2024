package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.exception.DataBaseError;
import edu.java.scrapper.exception.MissingChatException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
@Qualifier("JDBCChatRepository")
@Primary
public class JDBCChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final String CHAT_CREATION_ERROR_MESSAGE = "Chat was not created";
    private static final String CHAT_NOT_FOUND_ERROR_MESSAGE = "Chat not found";

    @Override
    public Chat add(Chat chat) {
        jdbcTemplate.update("INSERT INTO Chat (id, created_at) VALUES (?, ?)",
            chat.getId(), chat.getCreatedAt());
        return getById(chat.getId()).orElseThrow(() -> new DataBaseError(CHAT_CREATION_ERROR_MESSAGE));
    }

    @Override
    public Chat remove(Long id) {
        Chat deletedChat = getById(id).orElseThrow(() -> new MissingChatException(CHAT_NOT_FOUND_ERROR_MESSAGE));
        jdbcTemplate.update("DELETE FROM Chat WHERE id = ?", id);
        return deletedChat;
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM Chat",
            (rs, rowNum) -> mapRowToChat(rs));
    }

    @Override
    public Optional<Chat> getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM Chat WHERE id = ?",
                (rs, rowNum) -> Optional.of(mapRowToChat(rs)),
                id
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Chat mapRowToChat(ResultSet rs) throws SQLException {
        Chat chat = new Chat();
        chat.setId(rs.getLong("id"));
        OffsetDateTime createdAt = rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC);
        chat.setCreatedAt(createdAt);
        return chat;
    }
}
