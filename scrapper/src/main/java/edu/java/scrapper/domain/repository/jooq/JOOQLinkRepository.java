package edu.java.scrapper.domain.repository.jooq;


import edu.java.scrapper.domain.jooq.tables.records.LinkRecord;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.exception.DataBaseError;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.exception.LinkNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static edu.java.scrapper.domain.jooq.tables.Link.LINK;

@Repository
@RequiredArgsConstructor
@Qualifier("JOOQLinkRepository")
public class JOOQLinkRepository implements LinkRepository {

    private final DSLContext dslContext;

    @Override
    public Link add(URI uri) {
        OffsetDateTime now = OffsetDateTime.now();
        dslContext.insertInto(LINK)
            .set(LINK.URL, uri.toString())
            .set(LINK.CREATED_AT, now)
            .set(LINK.LAST_UPDATED_AT, now)
            .execute();

        return getLinkByUri(uri).orElseThrow(() -> new DataBaseError("Error creating link"));
    }

    @Override
    public Link remove(URI uri) {
        Link deletedLink = getLinkByUri(uri).orElseThrow(() -> new LinkNotFoundException("Link not found"));
        dslContext.deleteFrom(LINK)
            .where(LINK.URL.eq(uri.toString()))
            .execute();
        return deletedLink;
    }

    @Override
    public Link remove(long id) {
        Link deletedLink = getLinkById(id).orElseThrow(() -> new LinkNotFoundException("Link not found"));
        dslContext.deleteFrom(LINK)
            .where(LINK.ID.eq(id))
            .execute();
        return deletedLink;
    }

    @Override
    public Optional<Link> getLinkByUri(URI uri) {
        return dslContext.selectFrom(LINK)
            .where(LINK.URL.eq(uri.toString()))
            .fetchOptional()
            .map(this::mapRecordToLink);
    }

    @Override
    public Optional<Link> getLinkById(long id) {
        return dslContext.selectFrom(LINK)
            .where(LINK.ID.eq(id))
            .fetchOptional()
            .map(this::mapRecordToLink);
    }

    @Override
    public Link updateLastUpdatedTime(long id) {
        dslContext.update(LINK)
            .set(LINK.LAST_UPDATED_AT, OffsetDateTime.now())
            .where(LINK.ID.eq(id))
            .execute();

        return getLinkById(id).orElseThrow(() -> new LinkNotFoundException("Link not found"));
    }

    @Override
    public List<Link> findAll() {
        return dslContext.selectFrom(LINK)
            .fetch()
            .map(this::mapRecordToLink);
    }

    @Override
    public List<Link> findOldestLinks(int batchSize) {
        return dslContext.selectFrom(LINK)
            .orderBy(LINK.LAST_UPDATED_AT.asc())
            .limit(batchSize)
            .fetch()
            .map(this::mapRecordToLink);
    }

    private Link mapRecordToLink(LinkRecord record) {
        Link link = new Link();
        link.setId(record.getId());
        try {
            link.setUri(new URI(record.getUrl()));
        } catch (URISyntaxException e) {
            throw new InvalidLinkException("Invalid Link");
        }
        link.setCreatedAt(record.getCreatedAt());
        link.setLastUpdatedAt(record.getLastUpdatedAt());
        return link;
    }
}


