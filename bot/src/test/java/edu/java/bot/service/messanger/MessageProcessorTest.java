package edu.java.bot.service.messanger;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.commands.CommandConfigurator;
import edu.java.bot.service.commands.ICommand;
import edu.java.bot.service.messenger.MessageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MessageProcessorTest {
    private final CommandConfigurator configurator = Mockito.mock(CommandConfigurator.class);
    private final ICommand commandChain = Mockito.mock(ICommand.class);
    private final Update update = Mockito.mock(Update.class);
    private MessageProcessor messageProcessor;

    @BeforeEach
    public void setUp() {
        Mockito.when(configurator.configureCommands()).thenReturn(commandChain);
        messageProcessor = new MessageProcessor(configurator);
    }

    @Test
    @DisplayName("Проверка обработки сообщений")
    public void testProcessMessage() {
        // Given
        Mockito.when(commandChain.execute(update)).thenReturn("response");

        // When
        String response = messageProcessor.processMessage(update);

        // Then
        Mockito.verify(commandChain).execute(update);
        assert response.equals("response");
    }
}
