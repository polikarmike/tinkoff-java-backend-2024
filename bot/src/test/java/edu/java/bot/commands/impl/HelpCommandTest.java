package edu.java.bot.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.CommandHolder;
import edu.java.bot.commands.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class HelpCommandTest {
    private HelpCommand helpCommand;
    private CommandHolder commandHolder;

    @BeforeEach
    public void setup() {
        // Given
        commandHolder = Mockito.mock(CommandHolder.class);
        helpCommand = new HelpCommand(commandHolder);
    }

    @Test
    @DisplayName("Проверка выполнения команды /help")
    public void testExecute() {
        // Given
        Update update = Mockito.mock(Update.class);
        List<Command> commands = new ArrayList<>();
        Command mockCommand1 = Mockito.mock(Command.class);
        Command mockCommand2 = Mockito.mock(Command.class);

        when(mockCommand1.getName()).thenReturn("/command1");
        when(mockCommand1.getDescription()).thenReturn("Description of command1");
        when(mockCommand2.getName()).thenReturn("/command2");
        when(mockCommand2.getDescription()).thenReturn("Description of command2");

        commands.add(mockCommand1);
        commands.add(mockCommand2);

        when(commandHolder.getAllCommands()).thenReturn(commands);

        // When
        String result = helpCommand.execute(update);

        // Then
        String expectedResponse = """
            Вот список доступных команд:

            /command1 - Description of command1
            /command2 - Description of command2
            """;
        assertEquals(expectedResponse, result);
    }


    @Test
    @DisplayName("Проверка получения имени команды")
    public void testGetName() {
        // When
        String name = helpCommand.getName();

        // Then
        assertEquals("/help", name);
    }

    @Test
    @DisplayName("Проверка получения описания команды")
    public void testGetDescription() {
        // When
        String description = helpCommand.getDescription();

        // Then
        assertEquals("Отобразить список доступных команд и их описания", description);
    }
}
