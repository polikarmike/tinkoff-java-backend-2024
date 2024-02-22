package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;


public interface IUserMessageProcessor {
    SendMessage process(Update update);
}
