package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatLinkRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCLinkRepository;
import edu.java.scrapper.service.LinkUpdater;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.jdbc.JDBCLinkService;
import edu.java.scrapper.service.jdbc.JDBCLinkUpdater;
import edu.java.scrapper.service.jdbc.JDBCTgChatService;
import edu.java.scrapper.updater.UpdaterHolder;
import edu.java.scrapper.utils.linkverifier.LinkVerifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCAccessConfig {
    @Bean
    public JDBCLinkService linkService(JDBCLinkRepository linkRepository, JDBCChatRepository chatRepository,
        JDBCChatLinkRepository chatLinkRepository, LinkVerifier linkVerifier) {
        return new JDBCLinkService(linkRepository, chatRepository, chatLinkRepository, linkVerifier);
    }

    @Bean
    public TgChatService chatService(JDBCChatRepository chatRepository, JDBCChatLinkRepository chatLinkRepository,
        JDBCLinkService linkService) {
        return new JDBCTgChatService(chatRepository, chatLinkRepository, linkService);
    }

    @Bean
    public LinkUpdater linkUpdater(JDBCLinkRepository linkRepository, JDBCChatLinkRepository chatLinkRepository,
        UpdaterHolder updaterHolder, BotClient botClient) {
        return new JDBCLinkUpdater(linkRepository, chatLinkRepository, updaterHolder, botClient);
    }

}
