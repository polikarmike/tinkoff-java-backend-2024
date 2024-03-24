package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.domain.repository.jooq.JOOQChatLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQChatRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQLinkRepository;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.jooq.JOOQLinkService;
import edu.java.scrapper.service.jooq.JOOQLinkUpdater;
import edu.java.scrapper.service.jooq.JOOQTgChatService;
import edu.java.scrapper.updater.UpdaterHolder;
import edu.java.scrapper.utils.linkverifier.LinkVerifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JOOQAccessConfig {
    @Bean
    public JOOQLinkService linkService(
        JOOQLinkRepository linkRepository, JOOQChatRepository chatRepository,
        JOOQChatLinkRepository chatLinkRepository, LinkVerifier linkVerifier) {
        return new JOOQLinkService(linkRepository, chatRepository, chatLinkRepository, linkVerifier);
    }

    @Bean
    public TgChatService chatService(JOOQChatRepository chatRepository, JOOQChatLinkRepository chatLinkRepository,
        JOOQLinkService linkService) {
        return new JOOQTgChatService(chatRepository, chatLinkRepository, linkService);
    }

    @Bean
    public LinkUpdater linkUpdater(JOOQLinkRepository linkRepository, JOOQChatLinkRepository chatLinkRepository,
        UpdaterHolder updaterHolder, BotClient botClient) {
        return new JOOQLinkUpdater(linkRepository, chatLinkRepository, updaterHolder, botClient);
    }

}
