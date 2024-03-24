package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.commands.Command;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final String COMMAND_NAME = "/track";
    private static final String COMMAND_DESCRIPTION = "Начать отслеживание ссылки";
    private static final String MISSING_URL_MESSAGE = "Пожалуйста, предоставьте URL для отслеживания.";
    private static final String SUCCESS_MESSAGE = "Ссылка успешно добавлена: ";
    private static final String ERROR_MESSAGE = "Произошла ошибка при добавлении ссылки: ";

    private final ScrapperClient scrapperClient;

    @Override
    public String execute(Update update) {
        var tgChatId = update.message().chat().id();
        var messageText = update.message().text();

        try {

            String[] parts = messageText.split(" ", 2);
            if (parts.length < 2) {
                return MISSING_URL_MESSAGE;
            }

            String url = parts[1];

            URI uri = URI.create(url);
            scrapperClient.addLink(tgChatId, uri);

            log.info("Команда track выполнена");
            return SUCCESS_MESSAGE + url;
        } catch (Exception e) {
            log.error("Ошибка при добавлении ссылки для отслеживания");
            return ERROR_MESSAGE;
        }
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public String getDescription() {
        return COMMAND_DESCRIPTION;
    }
}
