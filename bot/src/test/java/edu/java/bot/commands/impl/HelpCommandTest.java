package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.commands.ICommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class HelpCommandTest {
    @InjectMocks
    private HelpCommand helpCommand;

    @Mock
    private ApplicationContext context;

    @Mock
    private CommandHolder commandHolder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Проверка выполнения команды /help")
    public void testExecute() {
        // Given
        Update update = Mockito.mock(Update.class);
        Map<String, ICommand> commands = new LinkedHashMap<>();
        ICommand mockCommand1 = Mockito.mock(ICommand.class);
        ICommand mockCommand2 = Mockito.mock(ICommand.class);
        when(mockCommand1.getName()).thenReturn("/command1");
        when(mockCommand1.getDescription()).thenReturn("Description of command1");
        when(mockCommand2.getName()).thenReturn("/command2");
        when(mockCommand2.getDescription()).thenReturn("Description of command2");
        commands.put("/command1", mockCommand1);
        commands.put("/command2", mockCommand2);

        when(commandHolder.getAllCommands()).thenReturn(commands);
        when(context.getBean(CommandHolder.class)).thenReturn(commandHolder);

        // When
        String result = helpCommand.execute(update);

        // Then
        String expectedResponse = """
            Вот список доступных команд:

            /command1 - Description of command1
            /command2 - Description of command2""";
        assertEquals(expectedResponse, result);
    }


    @Test
    @DisplayName("Проверка получения имени команды")
    public void testGetName() {
        // Given
        HelpCommand helpCommand = new HelpCommand();

        // When
        String name = helpCommand.getName();

        // Then
        assertEquals("/help", name);
    }

    @Test
    @DisplayName("Проверка получения описания команды")
    public void testGetDescription() {
        // Given
        HelpCommand helpCommand = new HelpCommand();

        // When
        String description = helpCommand.getDescription();

        // Then
        assertEquals("Отобразить список доступных команд и их описания", description);
    }
}
