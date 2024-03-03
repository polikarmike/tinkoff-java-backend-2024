package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.processor.SimpleUserMessageProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateTrackerBot  implements Bot {
    private final TelegramBot bot;
    private final SimpleUserMessageProcessor simpleUserMessageProcessor;
    private final CommandHolder commandHolder;

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        try {
            bot.execute(request);
        } catch (Exception e) {
            log.error("Error executing request", e);
        }
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() == null) {
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
            SendMessage response = simpleUserMessageProcessor.process(update);
            execute(response);
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @PostConstruct
    @Override
    public void start() {
        log.info("Starting UpdateTrackerBot...");
        bot.setUpdatesListener(this);
        createMenuCommands();
        log.info("UpdateTrackerBot started successfully.");
    }

    @PreDestroy
    @Override
    public void close() {
        log.info("Closing UpdateTrackerBot...");
        bot.removeGetUpdatesListener();
        log.info("UpdateTrackerBot closed successfully.");
    }

    private void createMenuCommands() {
        BotCommand[] menuCommands = commandHolder.getAllCommands().stream()
            .map(this::toApiCommand)
            .toArray(BotCommand[]::new);


        execute(new SetMyCommands(menuCommands));
        log.info("Menu commands created successfully.");

    }

    private BotCommand toApiCommand(Command command) {
        return new BotCommand(command.getName(), command.getDescription());
    }
}
