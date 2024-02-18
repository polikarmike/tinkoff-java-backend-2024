package edu.java.bot.service.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.helpers.MenuListCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class BotConfiguratorTest {
    private BotConfigurator botConfigurator;
    private final ApplicationConfig applicationConfig = Mockito.mock(ApplicationConfig.class);
    private final MenuListCreator menuListCreator = Mockito.mock(MenuListCreator.class);
    private final TelegramBot bot = Mockito.mock(TelegramBot.class);
    private final BotCommand[] commands = new BotCommand[0];

    @BeforeEach
    public void setUp() {
        when(applicationConfig.telegramToken()).thenReturn("testToken");
        when(menuListCreator.createBotCommands()).thenReturn(commands);
        botConfigurator = new BotConfigurator(applicationConfig, menuListCreator);
    }

    @Test
    @DisplayName("Проверка конфигурации бота")
    public void testBotConfiguration() {
        // Given
        botConfigurator = new BotConfigurator(applicationConfig, menuListCreator);

        // When
        TelegramBot resultBot = botConfigurator.getBot();

        // Then
        Assertions.assertNotNull(resultBot);
    }
}
