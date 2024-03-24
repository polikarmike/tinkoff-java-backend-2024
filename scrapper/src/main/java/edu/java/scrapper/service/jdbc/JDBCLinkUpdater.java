package edu.java.scrapper.service.jdbc;

import edu.java.common.dto.requests.LinkUpdateRequest;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatLinkRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCLinkRepository;
import edu.java.scrapper.dto.entity.jooq_jdbc.Link;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.updater.Updater;
import edu.java.scrapper.updater.UpdaterHolder;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


@RequiredArgsConstructor
public class JDBCLinkUpdater implements LinkUpdater {
    private final JDBCLinkRepository linkRepository;
    private final JDBCChatLinkRepository chatLinkRepository;
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
