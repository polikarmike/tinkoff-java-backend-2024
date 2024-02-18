package edu.java.bot.service.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.bot.BotConfigurator;
import edu.java.bot.service.messenger.MessageProcessor;
import edu.java.bot.service.messenger.MessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;


public class UpdateListenerTest {
    private BotConfigurator botConfigurator = Mockito.mock(BotConfigurator.class);
    private MessageProcessor messageProcessor = Mockito.mock(MessageProcessor.class);
    private MessageSender messageSender = Mockito.mock(MessageSender.class);
    private TelegramBot bot = Mockito.mock(TelegramBot.class);
    private Update update = Mockito.mock(Update.class);
    private Message message = Mockito.mock(Message.class);
    private Chat chat = Mockito.mock(Chat.class);

    private UpdateListener updateListener;

    @BeforeEach
    public void setUp() {
        Mockito.when(botConfigurator.getBot()).thenReturn(bot);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(123L);
        updateListener = new UpdateListener(botConfigurator, messageProcessor, messageSender);
    }

    @Test
    @DisplayName("Проверка обработки обновлений")
    public void testProcessUpdates() {
        // Given
        Mockito.when(messageProcessor.processMessage(update)).thenReturn("response");

        // When
        updateListener.processUpdates(Collections.singletonList(update));

        // Then
        Mockito.verify(messageSender).sendMessage(123L, "response");
    }
}
