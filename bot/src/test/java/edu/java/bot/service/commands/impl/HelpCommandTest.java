package edu.java.bot.service.commands.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class HelpCommandTest {
    private HelpCommand helpCommand;
    private final Update update = Mockito.mock(Update.class);
    private final Message message = Mockito.mock(Message.class);

    @BeforeEach
    public void setUp() {
        helpCommand = new HelpCommand();
        when(update.message()).thenReturn(message);
    }

    @Test
    @DisplayName("Проверка выполнения команды /help")
    public void testExecuteHelpCommand() {
        // Given
        when(message.text()).thenReturn("/help");

        // When
        String response = helpCommand.execute(update);

        // Then
        String expectedResponse = "Вот список доступных команд:\n\n"
            + "/start - Зарегистрировать пользователя\n"
            + "/help - Вывести это окно с командами\n"
            + "/track - Начать отслеживание ссылки\n"
            + "/untrack - Прекратить отслеживание ссылки\n"
            + "/list - Показать список отслеживаемых ссылок";
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Проверка выполнения другой команды")
    public void testExecuteOtherCommand() {
        // Given
        when(message.text()).thenReturn("/other");

        // When
        String response = helpCommand.execute(update);

        // Then
        assertEquals("Нет такой команды. Для справки введите /help.", response);
    }
}

