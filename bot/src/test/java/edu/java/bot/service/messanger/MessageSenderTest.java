package edu.java.bot.service.messanger;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.bot.BotConfigurator;
import edu.java.bot.service.messenger.MessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MessageSenderTest {
    private BotConfigurator botConfigurator = Mockito.mock(BotConfigurator.class);
    private TelegramBot bot = Mockito.mock(TelegramBot.class);

    private MessageSender messageSender;

    @BeforeEach
    public void setUp() {
        Mockito.when(botConfigurator.getBot()).thenReturn(bot);
        messageSender = new MessageSender(botConfigurator);
    }

    @Test
    @DisplayName("Проверка отправки сообщений")
    public void testSendMessage() {
        // Given
        long chatId = 123L;
        String text = "response";

        // When
        messageSender.sendMessage(chatId, text);

        // Then
        Mockito.verify(bot).execute(Mockito.any(SendMessage.class));
    }
}
