package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class TrackCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackCommand.class);
    private static final String COMMAND_NAME = "/track";
    private static final String COMMAND_DESCRIPTION = "Начать отслеживание ссылки";
    private static final String COMMAND_RESPONSE = "Здесь будет реализация команды /track";

    @Override
    public String execute(Update update) {
        LOGGER.info("Track command executed");
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
