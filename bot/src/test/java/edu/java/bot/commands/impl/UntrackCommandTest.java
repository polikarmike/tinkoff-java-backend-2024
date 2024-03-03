package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import edu.java.bot.client.scrapper.ScrapperClient;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.pengrad.telegrambot.model.Update;

public class UntrackCommandTest {
    @Test
    @DisplayName("Проверка выполнения команды /untrack")
    public void testExecuteUntrackCommand() {
        // Given
        ScrapperClient scrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.when(scrapperClient.getAllLinks(anyLong())).thenReturn(null);

        UntrackCommand untrackCommand = new UntrackCommand(scrapperClient);
        Update update = Mockito.mock(Update.class);
        Mockito.when(update.message()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(update.message().chat()).thenReturn(Mockito.mock(Chat.class));
        Mockito.when(update.message().chat().id()).thenReturn(12345L);


        // When
        String response = untrackCommand.execute(update);

        // Then
        assertEquals("Здесь будет реализация команды /untrack", response);
    }

    @Test
    @DisplayName("Проверка получения имени команды")
    public void testGetName() {
        // Given
        ScrapperClient scrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.when(scrapperClient.getAllLinks(anyLong())).thenReturn(null);

        UntrackCommand untrackCommand = new UntrackCommand(scrapperClient);

        // When
        String name = untrackCommand.getName();

        // Then
        assertEquals("/untrack", name);
    }

    @Test
    @DisplayName("Проверка получения описания команды")
    public void testGetDescription() {
        // Given
        ScrapperClient scrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.when(scrapperClient.getAllLinks(anyLong())).thenReturn(null);

        UntrackCommand untrackCommand = new UntrackCommand(scrapperClient);

        // When
        String description = untrackCommand.getDescription();

        // Then
        assertEquals("Прекратить отслеживание ссылки", description);
    }
}
