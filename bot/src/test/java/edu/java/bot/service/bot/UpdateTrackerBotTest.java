package edu.java.bot.service.bot;

import edu.java.bot.service.listener.UpdateListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class UpdateTrackerBotTest {
    private final UpdateListener updateListener = Mockito.mock(UpdateListener.class);

    @Test
    @DisplayName("Проверка запуска прослушивания обновлений")
    public void testStartListening() {
        // Given
        UpdateTrackerBot updateTrackerBot = new UpdateTrackerBot(updateListener);

        // When
        updateTrackerBot.start();

        // Then
        verify(updateListener).startListening();
    }
}
