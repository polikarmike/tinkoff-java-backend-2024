package edu.java.scrapper.service.jpa;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.domain.repository.jpa.JPAChatRepository;
import edu.java.scrapper.domain.repository.jpa.JPALinkRepository;
import edu.java.scrapper.dto.entity.jpa.Link;
import edu.java.scrapper.utils.linkverifier.LinkVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.Collection;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JPALinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private JPALinkRepository linkRepository;

    @Autowired
    private JPAChatRepository chatRepository;

    @Autowired
    private JPALinkService linkService;

    @Autowired
    private JPATgChatService jpaTgChatService;

    @MockBean
    private LinkVerifier linkVerifier;

    @Test
    @Transactional
    @Rollback
    @DisplayName("Добавление ссылки: успешно")
    public void testAddLink_Success() {
        long tgChatId = 1L;
        URI uri = URI.create("https://example.com");

        jpaTgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri)).thenReturn(true);

        Link result = linkService.add(tgChatId, uri);

        Assertions.assertTrue(linkRepository.exists(tgChatId, result.getId()));
    }



    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаление ссылки")
    public void testRemoveLink_Success() {
        long tgChatId = 1L;
        URI uri = URI.create("https://example.com");

        when(linkVerifier.checkLink(uri)).thenReturn(true);

        jpaTgChatService.register(1L);

        Link result = linkService.add(tgChatId, uri);

        linkService.remove(tgChatId, uri);

        Assertions.assertFalse(linkRepository.exists(tgChatId, result.getId()));
        Assertions.assertFalse(linkRepository.findById(result.getId()).isPresent());


    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Получение всех ссылок")
    public void testListAllLinks_Success() {
        long tgChatId = 1L;

        jpaTgChatService.register(1L);

        URI uri1 = URI.create("https://example.com/1");
        URI uri2 = URI.create("https://example.com/2");

        when(linkVerifier.checkLink(uri1)).thenReturn(true);
        when(linkVerifier.checkLink(uri2)).thenReturn(true);

        Link result1 = linkService.add(tgChatId, uri1);
        Link result2 = linkService.add(tgChatId, uri2);

        Collection<Link> links = linkService.listAll(tgChatId);

        Assertions.assertTrue(links.contains(result1));
        Assertions.assertTrue(links.contains(result2));

    }

}
