package edu.java.bot.commands.impl;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pengrad.telegrambot.model.Update;

public class ListCommandTest {
    @Test
    @DisplayName("Проверка выполнения команды /list")
    public void testExecuteListCommand() {
        // Given
        ListCommand listCommand = new ListCommand();
        Update update = Mockito.mock(Update.class);

        // When
        String response = listCommand.execute(update);

        // Then
        assertEquals("Здесь будет реализация команды /list", response);
    }

    @Test
    @DisplayName("Проверка получения имени команды")
    public void testGetName() {
        // Given
        ListCommand listCommand = new ListCommand();

        // When
        String name = listCommand.getName();

        // Then
        assertEquals("/list", name);
    }

    @Test
    @DisplayName("Проверка получения описания команды")
    public void testGetDescription() {
        // Given
        ListCommand listCommand = new ListCommand();

        // When
        String description = listCommand.getDescription();

        // Then
        assertEquals("Отобразить список отслеживаемых ссылок", description);
    }
}
