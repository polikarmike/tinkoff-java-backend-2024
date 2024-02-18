package edu.java.bot.service.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.bot.BotConfigurator;
import edu.java.bot.service.commands.impl.HelpCommand;
import edu.java.bot.service.messenger.MessageProcessor;
import edu.java.bot.service.messenger.MessageSender;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private final TelegramBot bot;
    private final MessageProcessor messageProcessor;
    private final MessageSender messageSender;

    @Autowired
    public UpdateListener(
        BotConfigurator botConfigurator,
                          MessageProcessor messageProcessor,
                          MessageSender messageSender) {
        this.bot = botConfigurator.getBot();
        this.messageProcessor = messageProcessor;
        this.messageSender = messageSender;
    }

    public void startListening() {
        bot.setUpdatesListener(updates -> {
            processUpdates(updates);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    void processUpdates(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null) {
                String response = messageProcessor.processMessage(update);
                messageSender.sendMessage(update.message().chat().id(), response);
                LOGGER.debug("Processed update and sent response to chat id: {}", update.message().chat().id());
            }
        }
    }
}
