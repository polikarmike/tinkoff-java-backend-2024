package edu.java.bot.commands;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandHolderTest {

    private CommandHolder commandHolder;

    @BeforeEach
    public void setUp() {
        Command command1 = mock(Command.class);
        when(command1.getName()).thenReturn("command1");

        Command command2 = mock(Command.class);
        when(command2.getName()).thenReturn("command2");

        List<Command> commandList = Arrays.asList(command1, command2);

        commandHolder = new CommandHolder(commandList);
    }

    @Test
    @DisplayName("Проверка получения существующей команды по имени")
    public void testGetCommandByName_ExistingCommand() {
        // Given
        String commandName = "command1";

        // When
        Optional<Command> optionalCommand = commandHolder.getCommandByName(commandName);

        // Then
        assertTrue(optionalCommand.isPresent());
        assertEquals("command1", optionalCommand.get().getName());
    }

    @Test
    @DisplayName("Проверка получения несуществующей команды по имени")
    public void testGetCommandByName_NonExistingCommand() {
        // Given
        String commandName = "nonExistingCommand";

        // When
        Optional<Command> optionalCommand = commandHolder.getCommandByName(commandName);

        // Then
        assertFalse(optionalCommand.isPresent());
    }

    @Test
    @DisplayName("Проверка получения всех команд")
    public void testGetAllCommands() {
        // When
        List<Command> allCommands = commandHolder.getAllCommands();

        // Then
        assertEquals(2, allCommands.size());
        assertTrue(allCommands.stream().anyMatch(command -> command.getName().equals("command1")));
        assertTrue(allCommands.stream().anyMatch(command -> command.getName().equals("command2")));
    }

    @Test
    @DisplayName("Проверка получения команд из пустого списка")
    public void testGetAllCommands_EmptyList() {
        // Given
        List<Command> emptyCommandList = Collections.emptyList();

        // When
        CommandHolder emptyCommandHolder = new CommandHolder(emptyCommandList);
        List<Command> allCommands = emptyCommandHolder.getAllCommands();

        // Then
        assertEquals(0, allCommands.size());
    }
}

