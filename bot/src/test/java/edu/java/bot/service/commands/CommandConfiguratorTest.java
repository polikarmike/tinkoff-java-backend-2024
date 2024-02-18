package edu.java.bot.service.commands;

import edu.java.bot.service.commands.impl.StartCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class CommandConfiguratorTest {
    private CommandConfigurator commandConfigurator;

    @Test
    @DisplayName("Проверка конфигурации команд")
    public void testConfigureCommands() {
        // Given
        commandConfigurator = new CommandConfigurator();

        // When
        ICommand commandChain = commandConfigurator.configureCommands();

        // Then
        assertTrue(commandChain instanceof StartCommand);
    }
}
