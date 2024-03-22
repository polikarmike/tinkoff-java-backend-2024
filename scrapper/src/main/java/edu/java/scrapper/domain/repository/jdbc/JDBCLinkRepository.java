package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.exception.DataBaseError;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.exception.LinkNotFoundException;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Qualifier("JDBCLinkRepository")
@Primary
public class JDBCLinkRepository  implements LinkRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String LINK_CREATION_ERROR_MESSAGE = "Link was not created";
    private static final String LINK_NOT_FOUND_ERROR_MESSAGE = "Link not found";
    private static final String INVALID_LINK_ERROR_MESSAGE = "Invalid Link";

    @Override
    public Link add(URI uri) {
        OffsetDateTime now = OffsetDateTime.now();
        jdbcTemplate.update("INSERT INTO Link (url, created_at, last_updated_at) VALUES (?, ?, ?)",
            uri.toString(), now, now);
        return getLinkByUri(uri).orElseThrow(() -> new DataBaseError(LINK_CREATION_ERROR_MESSAGE));
    }

    @Override
    public Link remove(URI uri) {
        Link deletedLink = getLinkByUri(uri).orElseThrow(() -> new LinkNotFoundException(LINK_NOT_FOUND_ERROR_MESSAGE));
        jdbcTemplate.update("DELETE FROM Link WHERE url = ?", uri.toString());
        return deletedLink;
    }

    @Override
    public Link remove(long id) {
        Link deletedLink = getLinkById(id).orElseThrow(() -> new LinkNotFoundException(LINK_NOT_FOUND_ERROR_MESSAGE));
        jdbcTemplate.update("DELETE FROM Link WHERE id = ?", id);
        return deletedLink;
    }

    @Override
    public Optional<Link> getLinkByUri(URI uri) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM Link WHERE url = ?",
                (rs, rowNum) -> Optional.of(mapRowToLink(rs)),
                uri.toString());
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Link> getLinkById(long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM Link WHERE id = ?",
            (rs, rowNum) -> Optional.of(mapRowToLink(rs)),
            id);
    }

    @Override
    public Link updateLastUpdatedTime(long id) {
        jdbcTemplate.update(
            "UPDATE Link SET last_updated_at = ? WHERE id = ?",
            OffsetDateTime.now(), id);

        return getLinkById(id).orElseThrow(() -> new LinkNotFoundException(LINK_NOT_FOUND_ERROR_MESSAGE));
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM Link",
            (rs, rowNum) -> mapRowToLink(rs));
    }

    @Override
    public List<Link> findOldestLinks(int batchSize) {
        return jdbcTemplate.query(
            "SELECT * FROM Link ORDER BY last_updated_at ASC LIMIT ?",
            (rs, rowNum) -> mapRowToLink(rs), batchSize
        );
    }

    private Link mapRowToLink(ResultSet rs) throws SQLException {
        Link link = new Link();
        link.setId(rs.getLong("id"));
        try {
            link.setUri(new URI(rs.getString("url")));
        } catch (URISyntaxException e) {
            throw new InvalidLinkException(INVALID_LINK_ERROR_MESSAGE);
        }
        link.setCreatedAt(convertTimestampToOffsetDateTime(rs.getTimestamp("created_at")));
        link.setLastUpdatedAt(convertTimestampToOffsetDateTime(rs.getTimestamp("last_updated_at")));
        return link;
    }

    private OffsetDateTime convertTimestampToOffsetDateTime(Timestamp timestamp) {
        return (timestamp != null) ? timestamp.toInstant().atOffset(ZoneOffset.UTC) : null;
    }
}
