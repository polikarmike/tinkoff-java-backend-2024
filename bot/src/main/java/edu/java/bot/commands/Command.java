package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;

public interface Command {
    String execute(Update update);

    String getName();

    String getDescription();
}
