package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
@Slf4j
public class ListCommand implements Command {
    private static final String COMMAND_NAME = "/list";
    private static final String COMMAND_DESCRIPTION = "Отобразить список отслеживаемых ссылок";
    private static final String COMMAND_RESPONSE = "Здесь будет реализация команды /list";

    @Override
    public String execute(Update update) {
        log.info("List command executed");
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
