package edu.java.scrapper.service;

import edu.java.scrapper.dto.entity.Link;
import java.net.URI;
import java.util.Collection;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, URI url);

    Link remove(long tgChatId, URI url);

    Link remove(long tgChatId, long id);

    Collection<Link> listAll(long tgChatId);

    List<Link> findOldestLinks(int batchSize);

    List<Long> getChatIdsByLinkId(Long id);

    void updateLastUpdatedTime(long id);

    int cleanUpUnusedLink();
}
