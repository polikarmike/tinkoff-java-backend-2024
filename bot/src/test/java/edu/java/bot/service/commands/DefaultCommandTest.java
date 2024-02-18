package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DefaultCommandTest {
    private DefaultCommand defaultCommand;
    private final Update update = Mockito.mock(Update.class);
    private final Message message = Mockito.mock(Message.class);

    @BeforeEach
    public void setUp() {
        defaultCommand = new DefaultCommand();
        when(update.message()).thenReturn(message);
    }

    @Test
    @DisplayName("Проверка выполнения команды по умолчанию")
    public void testExecuteDefaultCommand() {
        // Given
        when(message.text()).thenReturn("/unknown");

        // When
        String response = defaultCommand.execute(update);

        // Then
        assertEquals("Нет такой команды. Для справки введите /help.", response);
    }

    @Test
    @DisplayName("Проверка выполнения команды после установки следующей команды")
    public void testExecuteNextCommand() {
        // Given
        ICommand nextCommand = Mockito.mock(ICommand.class);
        when(nextCommand.execute(update)).thenReturn("Следующая команда выполнена успешно");
        defaultCommand.setNextCommand(nextCommand);
        when(message.text()).thenReturn("/somecommand");

        // When
        String response = defaultCommand.execute(update);

        // Then
        assertEquals("Следующая команда выполнена успешно", response);
    }
}
