package edu.java.bot.commands.impl;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.pengrad.telegrambot.model.Update;

public class StartCommandTest {

    @Test
    @DisplayName("Проверка выполнения команды /start")
    public void testExecuteStartCommand() {
        // Given
        StartCommand startCommand = new StartCommand();
        Update update = Mockito.mock(Update.class);

        // When
        String response = startCommand.execute(update);

        // Then
        assertEquals("Здесь будет реализация команды /start", response);
    }

    @Test
    @DisplayName("Проверка получения имени команды")
    public void testGetName() {
        // Given
        StartCommand startCommand = new StartCommand();

        // When
        String name = startCommand.getName();

        // Then
        assertEquals("/start", name);
    }

    @Test
    @DisplayName("Проверка получения описания команды")
    public void testGetDescription() {
        // Given
        StartCommand startCommand = new StartCommand();

        // When
        String description = startCommand.getDescription();

        // Then
        assertEquals("Начать работу с ботом и зарегистрироваться", description);
    }
}
