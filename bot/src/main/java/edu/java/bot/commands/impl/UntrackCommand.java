package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UntrackCommand implements Command {
    private static final String COMMAND_NAME = "/untrack";
    private static final String COMMAND_DESCRIPTION = "Прекратить отслеживание ссылки";
    private static final String COMMAND_RESPONSE = "Здесь будет реализация команды /untrack";

    @Override
    public String execute(Update update) {
        log.info("Track command executed");
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
