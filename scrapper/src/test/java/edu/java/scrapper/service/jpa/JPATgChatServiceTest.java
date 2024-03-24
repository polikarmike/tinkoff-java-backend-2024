package edu.java.scrapper.service.jpa;

import java.net.URI;
import java.util.Optional;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.domain.repository.jpa.JPAChatRepository;
import edu.java.scrapper.domain.repository.jpa.JPALinkRepository;
import edu.java.scrapper.dto.entity.jpa.Chat;
import edu.java.scrapper.dto.entity.jpa.Link;
import edu.java.scrapper.exception.MissingChatException;
import edu.java.scrapper.exception.RepeatedRegistrationException;
import edu.java.scrapper.utils.linkverifier.LinkVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JPATgChatServiceTest extends IntegrationEnvironment {

    @Autowired
    private JPAChatRepository chatRepository;

    @Autowired
    private JPALinkService linkService;
    @Autowired
    private JPALinkRepository linkRepository;

    @Autowired
    private JPATgChatService tgChatService;

    @MockBean
    private LinkVerifier linkVerifier;


    @Test
    @Transactional
    @Rollback
    @DisplayName("Регистрация чата")
    public void testRegisterChat_Success() {
        long tgChatId = 1L;

        tgChatService.register(tgChatId);

        Optional<Chat> savedChatOptional = chatRepository.findById(tgChatId);

        assertTrue(savedChatOptional.isPresent());
        assertEquals(tgChatId, savedChatOptional.get().getId());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Регистрация уже существующего чата")
    public void testRegisterChat_AlreadyExists() {
        long tgChatId = 1L;

        tgChatService.register(tgChatId);

        assertThrows(RepeatedRegistrationException.class, () -> tgChatService.register(tgChatId));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаление чата")
    public void testUnregisterChat_Success() {
        long tgChatId = 1L;

        tgChatService.register(tgChatId);
        tgChatService.unregister(tgChatId);

        Optional<Chat> deletedChatOptional = chatRepository.findById(tgChatId);

        assertFalse(deletedChatOptional.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаления несуществующего чата")
    public void testUnregisterChat_NotFound() {
        long tgChatId = 1L;

        assertThrows(MissingChatException.class, () -> tgChatService.unregister(tgChatId));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Удаления ссылок при удалении чата")
    public void testUnregisterChat_RemoveLinks() {
        long tgChatId = 1L;
        URI uri1 = URI.create("https://example.com/1");
        URI uri2 = URI.create("https://example.com/2");

        tgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri1)).thenReturn(true);
        when(linkVerifier.checkLink(uri2)).thenReturn(true);

        Link result1 = linkService.add(tgChatId, uri1);
        Link result2 = linkService.add(tgChatId, uri2);

        Assertions.assertTrue(linkRepository.findByUri(uri1).isPresent());
        Assertions.assertTrue(linkRepository.findByUri(uri2).isPresent());

        tgChatService.unregister(tgChatId);

        Assertions.assertTrue(linkRepository.findByUri(uri1).isEmpty());
        Assertions.assertTrue(linkRepository.findByUri(uri2).isEmpty());
    }
}
