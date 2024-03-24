package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatLinkRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCLinkRepository;
import edu.java.scrapper.dto.entity.jooq_jdbc.Link;
import edu.java.scrapper.updater.Updater;
import edu.java.scrapper.updater.UpdaterHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LinkUpdaterTest {

    @Mock
    private JDBCLinkRepository linkRepository;
    @Mock
    private JDBCChatLinkRepository chatLinkRepository;
    @Mock
    private UpdaterHolder updaterHolder;
    @Mock
    private BotClient botClient;
    @Mock
    private Updater updater;

    @InjectMocks
    private JDBCLinkUpdater linkUpdater;


    @Test
    public void testUpdate() {
        Link link = new Link();
        link.setId(123L);
        link.setUri(URI.create("http://example.com"));
        when(linkRepository.findOldestLinks(anyInt())).thenReturn(Arrays.asList(link));
        when(updaterHolder.getUpdaterByHost(anyString())).thenReturn(Optional.of(updater));
        when(updater.getUpdateMessage(any())).thenReturn("Update message");
        when(chatLinkRepository.getChatIdsByLinkId(anyLong())).thenReturn(Arrays.asList(1L, 2L, 3L));

        linkUpdater.update();

        verify(botClient, times(1)).sendUpdate(any());
        verify(linkRepository, times(1)).updateLastUpdatedTime(anyLong());
    }
}
