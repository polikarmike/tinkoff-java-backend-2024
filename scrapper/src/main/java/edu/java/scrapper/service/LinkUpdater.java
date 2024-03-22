package edu.java.scrapper.service;

import edu.java.common.dto.requests.LinkUpdateRequest;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.updater.Updater;
import edu.java.scrapper.updater.UpdaterHolder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdater {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final UpdaterHolder updaterHolder;
    private final BotClient botClient;

    @Value("${app.link-updater.batch-size}")
    private int batchSize;

    public int update() {
        List<Link> links = linkRepository.findOldestLinks(batchSize);
        int updatedCount = 0;
        for (Link link : links) {
            Optional<Updater> optionalUpdater = updaterHolder.getUpdaterByHost(link.getUri().getHost());
            Optional<String> message = optionalUpdater.map(updater -> updater.getUpdateMessage(link));

            if (message.isPresent()) {
                List<Long> chatsIds = chatLinkRepository.getChatIdsByLinkId(link.getId());
                botClient.sendUpdate(new LinkUpdateRequest(link.getId(), link.getUri(), message.get(), chatsIds));
            }

            linkRepository.updateLastUpdatedTime(link.getId());

            updatedCount++;
        }

        return updatedCount;
    }
}
