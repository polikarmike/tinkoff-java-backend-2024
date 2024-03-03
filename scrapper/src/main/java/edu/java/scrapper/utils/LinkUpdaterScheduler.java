package edu.java.scrapper.utils;

import edu.java.common.dto.requests.LinkUpdateRequest;
import edu.java.scrapper.client.bot.BotClient;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@ConditionalOnProperty(prefix = "app.scheduler", name = "enable", havingValue = "true")
@AllArgsConstructor
public class LinkUpdaterScheduler {
    private final BotClient botClient;


    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {

        Long id = 1L;
        URI url = URI.create("https://www.example.com");
        String description = "Example description";
        List<Long> tgChatIds =  new ArrayList<>();

        LinkUpdateRequest exampleRequest = new LinkUpdateRequest(id, url, description, tgChatIds);
        botClient.sendUpdate(exampleRequest);

        log.info("Update method executed");

    }
}
