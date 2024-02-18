package edu.java.bot.service.helpers;

import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.configuration.ApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MenuListCreatorTest {
    private MenuListCreator menuListCreator;
    private ApplicationConfig applicationConfig = Mockito.mock(ApplicationConfig.class);

    @BeforeEach
    public void setUp() {
        menuListCreator = new MenuListCreator(applicationConfig);
    }

    @Test
    @DisplayName("Проверка создания списка команд бота")
    public void testCreateBotCommands() {
        // Given
        Map<String, String> commandsMap = new HashMap<>();
        commandsMap.put("start", "Запустить бота");
        commandsMap.put("help", "Показать помощь");
        when(applicationConfig.commands()).thenReturn(commandsMap);

        // When
        BotCommand[] botCommands = menuListCreator.createBotCommands();

        // Then
        assertEquals(2, botCommands.length);
        assertEquals("/help", botCommands[0].command());
        assertEquals("Показать помощь", botCommands[0].description());
        assertEquals("/start", botCommands[1].command());
        assertEquals("Запустить бота", botCommands[1].description());
    }
}

