package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.commands.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SimpleUserMessageProcessorTest {
    private SimpleUserMessageProcessor simpleUserMessageProcessor;
    private CommandHolder commandHolder;
    private Update update;
    private Message message;
    private Chat chat;
    private Command command;

    @BeforeEach
    public void setUp() {
        // Given
        commandHolder = Mockito.mock(CommandHolder.class);
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
        command = Mockito.mock(Command.class);
        simpleUserMessageProcessor = new SimpleUserMessageProcessor(commandHolder);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
    }

    @Test
    @DisplayName("Проверка обработки сообщений")
    public void testProcess() {
        // Given
        when(message.text()).thenReturn("/command");
        when(commandHolder.getCommandByName("/command")).thenReturn(Optional.ofNullable(command));
        when(command.execute(update)).thenReturn("response");

        // When
        SendMessage result = simpleUserMessageProcessor.process(update);
        SendMessage expected = new SendMessage(123L, "response");

        // Then
        assertEquals(expected.getParameters().entrySet(), result.getParameters().entrySet());
    }

    @Test
    @DisplayName("Проверка обработки сообщений с несуществующей командой")
    public void testProcess_NonExistingCommand() {
        // Given
        when(message.text()).thenReturn("/nonExistingCommand");
        when(commandHolder.getCommandByName("/nonExistingCommand")).thenReturn(Optional.empty());

        // When
        SendMessage result = simpleUserMessageProcessor.process(update);
        SendMessage expected = new SendMessage(123L, "Команда не найдена. Для получения помощи наберите /help.");

        // Then
        assertEquals(expected.getParameters().entrySet(), result.getParameters().entrySet());
    }
}
