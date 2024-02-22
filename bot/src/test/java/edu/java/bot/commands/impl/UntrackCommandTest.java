package edu.java.bot.commands.impl;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.pengrad.telegrambot.model.Update;

public class UntrackCommandTest {
    @Test
    @DisplayName("Проверка выполнения команды /untrack")
    public void testExecuteUntrackCommand() {
        // Given
        UntrackCommand untrackCommand = new UntrackCommand();
        Update update = Mockito.mock(Update.class);

        // When
        String response = untrackCommand.execute(update);

        // Then
        assertEquals("Здесь будет реализация команды /untrack", response);
    }

    @Test
    @DisplayName("Проверка получения имени команды")
    public void testGetName() {
        // Given
        UntrackCommand untrackCommand = new UntrackCommand();

        // When
        String name = untrackCommand.getName();

        // Then
        assertEquals("/untrack", name);
    }

    @Test
    @DisplayName("Проверка получения описания команды")
    public void testGetDescription() {
        // Given
        UntrackCommand untrackCommand = new UntrackCommand();

        // When
        String description = untrackCommand.getDescription();

        // Then
        assertEquals("Прекратить отслеживание ссылки", description);
    }
}
