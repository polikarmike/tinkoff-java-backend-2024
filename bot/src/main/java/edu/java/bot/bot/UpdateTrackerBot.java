package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.commands.ICommand;
import edu.java.bot.processor.MessageProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateTrackerBot  implements IBot {
    // Сделал Логгер публичным для тестирования
    public static Logger logger = LoggerFactory.getLogger(UpdateTrackerBot.class);
    private final TelegramBot bot;
    private final MessageProcessor messageProcessor;
    private final CommandHolder commandHolder;

    public UpdateTrackerBot(TelegramBot bot, MessageProcessor messageProcessor, CommandHolder commandHolder) {
        this.bot = bot;
        this.messageProcessor = messageProcessor;
        this.commandHolder = commandHolder;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        try {
            bot.execute(request);
        } catch (Exception e) {
            logger.error("Error executing request", e);
        }
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null) {
                try {
                    SendMessage response = messageProcessor.process(update);
                    bot.execute(response);
                } catch (Exception e) {
                    logger.error("Error sending message response", e);
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @PostConstruct
    @Override
    public void start() {
        logger.info("Starting UpdateTrackerBot...");
        bot.setUpdatesListener(this);
        createMenuCommands();
        logger.info("UpdateTrackerBot started successfully.");
    }

    @PreDestroy
    @Override
    public void close() {
        logger.info("Closing UpdateTrackerBot...");
        bot.removeGetUpdatesListener();
        logger.info("UpdateTrackerBot closed successfully.");
    }

    private void createMenuCommands() {
        BotCommand[] menuCommands = commandHolder.getAllCommands().values().stream()
            .map(ICommand::toApiCommand)
            .toArray(BotCommand[]::new);

        try {
            bot.execute(new SetMyCommands(menuCommands));
            logger.info("Menu commands created successfully.");
        } catch (Exception e) {
            logger.error("Error creating menu commands", e);
        }
    }
}
