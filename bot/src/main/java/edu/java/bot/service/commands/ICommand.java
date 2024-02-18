package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;

public interface ICommand {
    ICommand setNextCommand(ICommand command);

    String execute(Update update);
}
