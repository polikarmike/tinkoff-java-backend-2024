package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.commands.ICommand;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor implements IUserMessageProcessor {
    private final CommandHolder commandHolder;

    public MessageProcessor(CommandHolder commandHolder) {
        this.commandHolder = commandHolder;
    }

    public SendMessage process(Update update) {
        ICommand command = commandHolder.getCommandByName(update.message().text());
        return new SendMessage(update.message().chat().id(), command.execute(update));
    }
}
