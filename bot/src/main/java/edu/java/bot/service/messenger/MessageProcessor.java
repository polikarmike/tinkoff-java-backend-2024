package edu.java.bot.service.messenger;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.commands.CommandConfigurator;
import edu.java.bot.service.commands.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {
    private CommandConfigurator configurator = new CommandConfigurator();

    @Autowired
    public MessageProcessor(CommandConfigurator configurator) {
        this.configurator = configurator;
    }

    public String processMessage(Update update) {
        ICommand commandChain = configurator.configureCommands();
        return commandChain.execute(update);
    }
}
