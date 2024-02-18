package edu.java.bot.service.helpers;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.commands.impl.HelpCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuListCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private final ApplicationConfig applicationConfig;

    @Autowired
    public MenuListCreator(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public BotCommand[] createBotCommands() {
        LOGGER.info("Main bot commands have been created successfully");
        return applicationConfig.commands().entrySet().stream()
            .map(entry -> new BotCommand("/" + entry.getKey(), entry.getValue()))
            .toArray(BotCommand[]::new);
    }
}
