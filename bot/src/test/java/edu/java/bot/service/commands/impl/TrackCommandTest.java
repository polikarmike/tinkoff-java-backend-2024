package edu.java.bot.service.commands.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TrackCommandTest {
    private TrackCommand trackCommand;
    private final Update update = Mockito.mock(Update.class);
    private final Message message = Mockito.mock(Message.class);

    @BeforeEach
    public void setUp() {
        trackCommand = new TrackCommand();
        when(update.message()).thenReturn(message);
    }

    @Test
    @DisplayName("Проверка выполнения команды /track")
    public void testExecuteTrackCommand() {
        // Given
        when(message.text()).thenReturn("/track");

        // When
        String response = trackCommand.execute(update);

        // Then
        assertEquals("Здесь будет реализация команды /track", response);
    }

    @Test
    @DisplayName("Проверка выполнения другой команды")
    public void testExecuteOtherCommand() {
        // Given
        when(message.text()).thenReturn("/other");

        // When
        String response = trackCommand.execute(update);

        // Then
        assertEquals("Нет такой команды. Для справки введите /help.", response);
    }
}
