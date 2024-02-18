package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.commands.impl.HelpCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private static final String DEFAULT_RESPONSE = "Нет такой команды. Для справки введите /help.";
    private ICommand nextCommand;

    public DefaultCommand() {
        nextCommand = null;
    }

    @Override
    public ICommand setNextCommand(ICommand command) {
        nextCommand = command;
        return command;
    }

    @Override
    public String execute(Update update) {
        if (nextCommand != null) {
            return nextCommand.execute(update);
        }
        LOGGER.warn("Invalid command: {}", update.message().text());
        return DEFAULT_RESPONSE;
    }
}
