package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.dto.entity.Chat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class JDBCChatRepository {

    private final JdbcTemplate jdbcTemplate;

    public void add(Chat chat) {
        jdbcTemplate.update("INSERT INTO Chat (id, created_at) VALUES (?, ?)",
            chat.getId().intValue(), chat.getCreatedAt());
    }

    public void remove(Long id) {
        jdbcTemplate.update("DELETE FROM Chat WHERE id = ?", id);
    }

    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM Chat",
            (rs, rowNum) -> mapRowToChat(rs));
    }

    public Optional<Chat> getById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Chat WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) -> Optional.of(mapRowToChat(rs)));
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
