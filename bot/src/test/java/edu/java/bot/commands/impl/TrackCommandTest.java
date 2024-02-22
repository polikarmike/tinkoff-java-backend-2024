package edu.java.bot.commands.impl;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.pengrad.telegrambot.model.Update;

public class TrackCommandTest {
    @Test
    @DisplayName("Проверка выполнения команды /track")
    public void testExecuteTrackCommand() {
        // Given
        TrackCommand trackCommand = new TrackCommand();
        Update update = Mockito.mock(Update.class);

        // When
        String response = trackCommand.execute(update);

        // Then
        assertEquals("Здесь будет реализация команды /track", response);
    }

    @Test
    @DisplayName("Проверка получения имени команды")
    public void testGetName() {
        // Given
        TrackCommand trackCommand = new TrackCommand();

        // When
        String name = trackCommand.getName();

        // Then
        assertEquals("/track", name);
    }

    @Test
    @DisplayName("Проверка получения описания команды")
    public void testGetDescription() {
        // Given
        TrackCommand trackCommand = new TrackCommand();

        // When
        String description = trackCommand.getDescription();

        // Then
        assertEquals("Начать отслеживание ссылки", description);
    }
}
