package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.commands.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String COMMAND_NAME = "/start";
    private static final String COMMAND_DESCRIPTION = "Начать работу с ботом и зарегистрироваться";
    private static final String REGISTRATION_SUCCESS_MESSAGE = "Чат успешно зарегистрирован.";
    private static final String REGISTRATION_ERROR_MESSAGE = "Произошла ошибка при регистрации чата: ";

    private final ScrapperClient scrapperClient;

    @Override
    public String execute(Update update) {
        var tgChatId = update.message().chat().id();

        try {
            scrapperClient.registerChat(tgChatId);
            log.info("Команда start выполнена");
            return REGISTRATION_SUCCESS_MESSAGE;
        } catch (Exception e) {
            log.error("Ошибка при регистрации чата");
            return REGISTRATION_ERROR_MESSAGE + e.getMessage();
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
