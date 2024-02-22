package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.processor.MessageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateTrackerBotTest {

    private TelegramBot telegramBot;
    private MessageProcessor messageProcessor;
    private CommandHolder commandHolder;
    private Logger logger;

    private UpdateTrackerBot updateTrackerBot;

    @BeforeEach
    void setUp() {
        // Given
        telegramBot = Mockito.mock(TelegramBot.class);
        messageProcessor = Mockito.mock(MessageProcessor.class);
        commandHolder = Mockito.mock(CommandHolder.class);
        logger = Mockito.mock(Logger.class);
        updateTrackerBot = new UpdateTrackerBot(telegramBot, messageProcessor, commandHolder);
        UpdateTrackerBot.logger = logger;
    }

    @Test
    @DisplayName("Проверка выполнения")
     void testExecute(){
     // Given
     SendMessage sendMessageRequest=new SendMessage(123, "Test message");

     // When
     updateTrackerBot.execute(sendMessageRequest);

     // Then
     verify(telegramBot).execute(sendMessageRequest);
     }

     @Test
     @DisplayName("Проверка обработки")
     void testProcess(){
     // Given
     SendMessage response=new SendMessage(123, "Response");
     Update update=Mockito.mock(Update.class);
     Message message=Mockito.mock(Message.class);
     when(update.message()).thenReturn(message);
     when(messageProcessor.process(update)).thenReturn(response);

     // When
     updateTrackerBot.process(Collections.singletonList(update));

     // Then
     ArgumentCaptor<SendMessage>sendMessageCaptor = ArgumentCaptor.forClass(SendMessage.class);
     verify(telegramBot).execute(sendMessageCaptor.capture());
     assertEquals(response, sendMessageCaptor.getValue());
    }

    @Test
    @DisplayName("Проверка запуска")
    void testStart() {
        // When
        updateTrackerBot.start();

        // Then
        verify(telegramBot).setUpdatesListener(updateTrackerBot);
        verify(logger).info("UpdateTrackerBot started successfully.");
    }

    @Test
    @DisplayName("Проверка закрытия")
    void testClose() {
        // When
        updateTrackerBot.close();

        // Then
        verify(telegramBot).removeGetUpdatesListener();
        verify(logger).info("UpdateTrackerBot closed successfully.");
    }
}


