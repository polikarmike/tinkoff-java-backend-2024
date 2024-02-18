package edu.java.bot.service.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.commands.DefaultCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ListCommand extends DefaultCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private static final String LIST_COMMAND = "/list";
    private static final String LIST_RESPONSE = "Здесь будет реализация команды /list";

    @Override
    public String execute(Update update) {
        if (update.message().text().equals(LIST_COMMAND)) {
            LOGGER.info("List command executed");
            return LIST_RESPONSE;
        }

        return super.execute(update);
    }
}
