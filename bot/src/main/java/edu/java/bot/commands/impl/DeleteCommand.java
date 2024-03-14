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
public class DeleteCommand implements Command {
    private static final String COMMAND_NAME = "/delete";
    private static final String COMMAND_DESCRIPTION = "Прекратить работу и удалить данные";
    private static final String COMMAND_RESPONSE = "Здесь будет реализация команды /delete";
    private final ScrapperClient scrapperClient;

    @Override
    public String execute(Update update) {
        var tgChatId = update.message().chat().id();
        scrapperClient.deleteChat(tgChatId);
        log.info("Delete command executed");
        return COMMAND_RESPONSE;
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
