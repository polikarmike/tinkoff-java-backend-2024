package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;

public interface ICommand {
    String execute(Update update);

    String getName();

    String getDescription();

    default BotCommand toApiCommand() {
        return new BotCommand(getName(), getDescription());
    }
}
