package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandHolder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class HelpCommand implements Command {
    private static final String COMMAND_NAME = "/help";
    private static final String COMMAND_DESCRIPTION = "Отобразить список доступных команд и их описания";
    private static final String COMMAND_LIST_INTRO = "Вот список доступных команд:\n";
    private final CommandHolder commandHolder;

    public HelpCommand(@Lazy CommandHolder commandHolder) {
        this.commandHolder = commandHolder;
    }

    @Override
    public String execute(Update update) {
        List<Command> commands = commandHolder.getAllCommands();

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(COMMAND_LIST_INTRO).append("\n");
        for (Command command : commands) {
            responseBuilder.append(command.getName()).append(" - ").append(command.getDescription()).append("\n");
        }

        log.info("Help command executed");
        return responseBuilder.toString();
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

