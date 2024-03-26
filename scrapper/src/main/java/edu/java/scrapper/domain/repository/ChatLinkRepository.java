package edu.java.scrapper.domain.repository;

import java.util.List;

public interface ChatLinkRepository {
    int add(Long chatId, Long linkId);

    int remove(Long chatId, Long linkId);

    List<Long> getLinkIdsByChatId(Long chatId);

    List<Long> getChatIdsByLinkId(Long linkId);

    boolean exists(Long chatId, Long linkId);

    int cleanupUnusedLinks();
}
