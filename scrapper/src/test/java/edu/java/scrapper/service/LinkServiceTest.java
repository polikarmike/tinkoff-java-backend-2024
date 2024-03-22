package edu.java.scrapper.service;

import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.utils.linkverifier.LinkVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LinkServiceTest {

    @Mock
    private LinkRepository linkRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private ChatLinkRepository chatLinkRepository;
    @Mock
    private LinkVerifier linkVerifier;
    @InjectMocks
    private LinkService linkService;

    @Test
    public void testAdd() {
        long tgChatId = 123L;
        URI uri = URI.create("http://example.com");

        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());

        Link link = new Link();
        link.setId(1L);
        link.setUri(uri);
        link.setLastUpdatedAt(OffsetDateTime.now());

        when(linkVerifier.checkLink(uri)).thenReturn(true);
        when(chatRepository.getById(tgChatId)).thenReturn(Optional.of(chat));
        when(linkRepository.getLinkByUri(uri)).thenReturn(Optional.of(link));
        when(chatLinkRepository.exists(anyLong(), anyLong())).thenReturn(false);

        linkService.add(tgChatId, uri);

        verify(chatLinkRepository, times(1)).add(anyLong(), anyLong());
    }

    @Test
    public void testRemoveByUri() {
        long tgChatId = 123L;
        URI uri = URI.create("http://example.com");

        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());

        Link link = new Link();
        link.setId(1L);
        link.setUri(uri);
        link.setLastUpdatedAt(OffsetDateTime.now());

        when(chatRepository.getById(tgChatId)).thenReturn(Optional.of(chat));
        when(linkRepository.getLinkByUri(uri)).thenReturn(Optional.of(link));
        when(chatLinkRepository.exists(anyLong(), anyLong())).thenReturn(true);
        when(chatLinkRepository.getChatIdsByLinkId(anyLong())).thenReturn(Arrays.asList(1L, 2L, 3L));

        linkService.remove(tgChatId, uri);

        verify(chatLinkRepository, times(1)).remove(anyLong(), anyLong());
    }

    @Test
    public void testRemoveById() {
        long tgChatId = 123L;
        long id = 456L;
        URI uri = URI.create("http://example.com");

        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());

        Link link = new Link();
        link.setId(id);
        link.setUri(uri);
        link.setLastUpdatedAt(OffsetDateTime.now());

        when(chatRepository.getById(tgChatId)).thenReturn(Optional.of(chat));
        when(linkRepository.getLinkById(id)).thenReturn(Optional.of(link));
        when(chatLinkRepository.exists(anyLong(), anyLong())).thenReturn(true);
        when(chatLinkRepository.getChatIdsByLinkId(anyLong())).thenReturn(Arrays.asList(1L, 2L, 3L));

        linkService.remove(tgChatId, id);

        verify(chatLinkRepository, times(1)).remove(anyLong(), anyLong());
    }

    @Test
    public void testListAll() {
        long tgChatId = 123L;
        URI uri = URI.create("http://example.com");

        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());

        Link link = new Link();
        link.setId(1L);
        link.setUri(uri);
        link.setLastUpdatedAt(OffsetDateTime.now());

        when(chatRepository.getById(tgChatId)).thenReturn(Optional.of(chat));
        when(chatLinkRepository.getLinkIdsByChatId(anyLong())).thenReturn(Arrays.asList(1L, 2L, 3L));
        when(linkRepository.getLinkById(anyLong())).thenReturn(Optional.of(link));

        linkService.listAll(tgChatId);

        verify(linkRepository, times(3)).getLinkById(anyLong());
    }
}
