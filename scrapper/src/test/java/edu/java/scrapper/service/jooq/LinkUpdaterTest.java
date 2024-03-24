package edu.java.scrapper.service.jooq;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.domain.repository.jooq.JOOQChatLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQLinkRepository;
import edu.java.scrapper.dto.entity.jooq_jdbc.Link;
import edu.java.scrapper.updater.Updater;
import edu.java.scrapper.updater.UpdaterHolder;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkUpdaterTest {

    @Mock
    private JOOQLinkRepository linkRepository;
    @Mock
    private JOOQChatLinkRepository chatLinkRepository;
    @Mock
    private UpdaterHolder updaterHolder;
    @Mock
    private BotClient botClient;
    @Mock
    private Updater updater;

    @InjectMocks
    private JOOQLinkUpdater linkUpdater;


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
