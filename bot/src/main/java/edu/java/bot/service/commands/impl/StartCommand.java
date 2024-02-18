package edu.java.bot.service.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.commands.DefaultCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StartCommand extends DefaultCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private static final String START_COMMAND = "/start";
    private static final String START_RESPONSE = "Здесь будет реализация команды /start";

    @Override
    public String execute(Update update) {
        if (update.message().text().equals(START_COMMAND)) {
            LOGGER.info("Start command executed");
            return START_RESPONSE;
        }
        return super.execute(update);
    }
}
