package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.Bot;
import edu.java.common.dto.requests.LinkUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkUpdateService {
    private final Bot bot;

    public void processLinkUpdate(LinkUpdateRequest linkUpdateRequest) {
        for (Long chatId : linkUpdateRequest.tgChatIds()) {
            bot.execute(new SendMessage(chatId, "Уведомление из Scrapper"));
        }
    }
}
