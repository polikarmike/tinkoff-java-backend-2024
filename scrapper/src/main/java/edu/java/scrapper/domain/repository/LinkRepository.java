package edu.java.scrapper.domain.repository;

import edu.java.scrapper.dto.entity.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    Link add(URI uri);

    Link remove(URI uri);

    Link remove(long id);

    Optional<Link> getLinkByUri(URI uri);

    Optional<Link> getLinkById(long id);

    Link updateLastUpdatedTime(long id);

    List<Link> findAll();

    List<Link> findOldestLinks(int batchSize);
}
