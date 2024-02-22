package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.commands.ICommand;
import java.util.Map;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class HelpCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private static final String COMMAND_NAME = "/help";
    private static final String COMMAND_DESCRIPTION = "Отобразить список доступных команд и их описания";
    private static final String COMMAND_LIST_INTRO = "Вот список доступных команд:\n";

    @Autowired
    private ApplicationContext context;

    @Override
    public String execute(Update update) {
        LOGGER.info("Help command executed");
        Map<String, ICommand> commands = context.getBean(CommandHolder.class).getAllCommands();
        StringJoiner responseJoiner = new StringJoiner("\n");
        responseJoiner.add(COMMAND_LIST_INTRO);
        commands.forEach((name, command) -> {
            responseJoiner.add(command.getName() + " - " + command.getDescription());
        });
        return responseJoiner.toString();
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

