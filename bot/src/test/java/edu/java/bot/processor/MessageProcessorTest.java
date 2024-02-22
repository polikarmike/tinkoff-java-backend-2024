package edu.java.bot.processor;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.commands.ICommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MessageProcessorTest {
    private MessageProcessor messageProcessor;
    private CommandHolder commandHolder;
    private Update update;
    private Message message;
    private Chat chat;
    private ICommand command;

    @BeforeEach
    public void setUp() {
        // Given
        commandHolder = Mockito.mock(CommandHolder.class);
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
        command = Mockito.mock(ICommand.class);
        messageProcessor = new MessageProcessor(commandHolder);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(message.text()).thenReturn("/command");
        when(commandHolder.getCommandByName("/command")).thenReturn(command);
        when(command.execute(update)).thenReturn("response");
    }

    @Test
    @DisplayName("Проверка обработки сообщений")
    public void testProcess() {
        // When
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(123L, "response");

        // Then
        assertEquals(expected.getParameters().entrySet(), result.getParameters().entrySet());
    }
}
