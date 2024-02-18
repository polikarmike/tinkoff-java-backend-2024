package edu.java.bot.service.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.commands.DefaultCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TrackCommand extends DefaultCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private static final String TRACK_COMMAND = "/track";
    private static final String TRACK_RESPONSE = "Здесь будет реализация команды /track";

    @Override
    public String execute(Update update) {
        if (update.message().text().equals(TRACK_COMMAND)) {
            LOGGER.info("Track command executed");
            return TRACK_RESPONSE;
        }
        return super.execute(update);
    }
}
