package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class ListCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListCommand.class);
    private static final String COMMAND_NAME = "/list";
    private static final String COMMAND_DESCRIPTION = "Отобразить список отслеживаемых ссылок";
    private static final String COMMAND_RESPONSE = "Здесь будет реализация команды /list";

    @Override
    public String execute(Update update) {
        LOGGER.info("List command executed");
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
