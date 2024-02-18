package edu.java.bot.service.messenger;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.bot.BotConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private final TelegramBot bot;

    @Autowired
    public MessageSender(BotConfigurator botConfigurator) {
        this.bot =  botConfigurator.getBot();
    }

    public void sendMessage(long chatId, String text) {
        bot.execute(new SendMessage(chatId, text));
    }
}
