package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);
    private static final String COMMAND_NAME = "/start";
    private static final String COMMAND_DESCRIPTION = "Начать работу с ботом и зарегистрироваться";
    private static final String COMMAND_RESPONSE = "Здесь будет реализация команды /start";

    @Override
    public String execute(Update update) {
        log.info("Start command executed");
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
