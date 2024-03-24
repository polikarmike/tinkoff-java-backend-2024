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
public class UntrackCommand implements Command {
    private static final String COMMAND_NAME = "/untrack";
    private static final String COMMAND_DESCRIPTION = "Прекратить отслеживание ссылки";
    private static final String MISSING_URL_MESSAGE = "Пожалуйста, предоставьте URL для удаления.";
    private static final String SUCCESS_MESSAGE = "Ссылка успешно удалена: ";
    private static final String ERROR_MESSAGE = "Произошла ошибка при удалении ссылки : ";

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

            scrapperClient.removeLink(tgChatId, uri);

            log.info("Команда untrack выполнена");
            return SUCCESS_MESSAGE + url;
        } catch (Exception e) {
            log.error("Ошибка при удалении ссылки из отслеживания");
            return ERROR_MESSAGE + e.getMessage();
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
