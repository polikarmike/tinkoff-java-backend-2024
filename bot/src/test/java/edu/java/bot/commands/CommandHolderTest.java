package edu.java.bot.commands;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import edu.java.bot.commands.impl.HelpCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandHolderTest {

    private CommandHolder commandHolder;
    private HelpCommand helpCommand;


    @BeforeEach
    public void setUp() {
        ICommand command1 = mock(ICommand.class);
        when(command1.getName()).thenReturn("command1");

        ICommand command2 = mock(ICommand.class);
        when(command2.getName()).thenReturn("command2");

        helpCommand = mock(HelpCommand.class);

        List<ICommand> commandList = Arrays.asList(command1, command2);

        commandHolder = new CommandHolder(commandList, helpCommand);
    }

    @Test
    @DisplayName("Проверка получения существующей команды по имени")
    public void testGetCommandByName_ExistingCommand() {
        // Given
        String commandName = "command1";

        // When
        ICommand command = commandHolder.getCommandByName(commandName);

        // Then
        assertEquals("command1", command.getName());
    }

    @Test
    @DisplayName("Проверка получения несуществующей команды по имени")
    public void testGetCommandByName_NonExistingCommand() {
        // Given
        String commandName = "nonExistingCommand";

        // When
        ICommand command = commandHolder.getCommandByName(commandName);

        // Then
        assertEquals(helpCommand, command);
    }

    @Test
    @DisplayName("Проверка получения всех команд")
    public void testGetAllCommands() {
        // When
        Map<String, ICommand> allCommands = commandHolder.getAllCommands();

        // Then
        assertEquals(2, allCommands.size());
        assertEquals("command1", allCommands.get("command1").getName());
        assertEquals("command2", allCommands.get("command2").getName());
    }

    @Test
    @DisplayName("Проверка получения команд из пустого списка")
    public void testGetAllCommands_EmptyList() {
        // Given
        List<ICommand> emptyCommandList = Collections.emptyList();

        // When
        CommandHolder emptyCommandHolder = new CommandHolder(emptyCommandList, helpCommand);
        Map<String, ICommand> allCommands = emptyCommandHolder.getAllCommands();

        // Then
        assertEquals(0, allCommands.size());
    }
}

