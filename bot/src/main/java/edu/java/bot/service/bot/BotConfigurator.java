package edu.java.bot.service.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.commands.impl.HelpCommand;
import edu.java.bot.service.helpers.MenuListCreator;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotConfigurator {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private final TelegramBot bot;
    private final String telegramToken;
    private final BotCommand[] commands;

    @Autowired
    public BotConfigurator(ApplicationConfig applicationConfig, MenuListCreator menuListCreator) {
        this.telegramToken = applicationConfig.telegramToken();
        this.bot = new TelegramBot(telegramToken);
        this.commands = menuListCreator.createBotCommands();
    }

    @PostConstruct
    public void setMainMenuCommands() {
        SetMyCommands setMyCommands = new SetMyCommands(commands);
        bot.execute(setMyCommands);
        LOGGER.info("Main bot commands have been set successfully");
    }

    public TelegramBot getBot() {
        return bot;
    }
}
