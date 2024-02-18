package edu.java.bot.service.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.commands.DefaultCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UntrackCommand extends DefaultCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private static final String UNTRACK_COMMAND = "/untrack";
    private static final String UNTRACK_RESPONSE = "Здесь будет реализация команды /untrack";

    @Override
    public String execute(Update update) {
        if (update.message().text().equals(UNTRACK_COMMAND)) {
            LOGGER.info("Untrack command executed");
            return UNTRACK_RESPONSE;
        }
        return super.execute(update);
    }
}
