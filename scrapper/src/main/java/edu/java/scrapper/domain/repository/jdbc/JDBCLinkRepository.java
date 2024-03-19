package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.dto.entity.Link;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JDBCLinkRepository {

    private final JdbcTemplate jdbcTemplate;

    public Link add(URI uri) {
        jdbcTemplate.update("INSERT INTO Link (url, created_at, last_updated_at) VALUES (?, ?, ?)",
            uri.toString(), OffsetDateTime.now(), OffsetDateTime.now());

        return getLinkByUri(uri).get();
    }

    public void remove(URI uri) {
        jdbcTemplate.update("DELETE FROM Link WHERE url = ?", uri.toString());
    }

    public Optional<Link> getLinkByUri(URI uri) {
        try {
            Link link = jdbcTemplate.queryForObject(
                "SELECT * FROM Link WHERE url = ?", new Object[]{uri.toString()}, (rs, rowNum) -> mapRowToLink(rs));
            return Optional.ofNullable(link);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Link> getLinkById(long id) {
        try {
            Link link = jdbcTemplate.queryForObject(
                "SELECT * FROM Link WHERE id = ?", new Object[]{id}, (rs, rowNum) -> mapRowToLink(rs));
            return Optional.ofNullable(link);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateLastUpdatedTime(long id) {
        jdbcTemplate.update("UPDATE Link SET last_updated_at = ? WHERE id = ?",
            OffsetDateTime.now(), id);
    }

    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM Link", (rs, rowNum) -> mapRowToLink(rs));
    }

    public List<Link> findByLastUpdateBefore(OffsetDateTime lastUpdateThreshold) {
        return jdbcTemplate.query("SELECT * FROM Link WHERE last_updated_at < ?",
            new Object[]{lastUpdateThreshold}, (rs, rowNum) -> mapRowToLink(rs));
    }

    private Link mapRowToLink(ResultSet rs) throws SQLException {
        Link link = new Link();
        link.setId(rs.getLong("id"));
        try {
            link.setUri(new URI(rs.getString("url")));
        } catch (URISyntaxException e) {
            log.error("Error parsing URL from database: {}", e.getMessage());
        }
        link.setCreatedAt(convertTimestampToOffsetDateTime(rs.getTimestamp("created_at")));
        link.setLastUpdatedAt(convertTimestampToOffsetDateTime(rs.getTimestamp("last_updated_at")));
        return link;
    }

    private OffsetDateTime convertTimestampToOffsetDateTime(Timestamp timestamp) {
        return (timestamp != null) ? timestamp.toInstant().atOffset(ZoneOffset.UTC) : null;
    }
}
