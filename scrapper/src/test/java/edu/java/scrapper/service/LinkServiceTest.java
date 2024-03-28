package edu.java.scrapper.service;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatLinkRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQChatLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQChatRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQLinkRepository;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.service.jdbc.JDBCLinkService;
import edu.java.scrapper.service.jdbc.JDBCTgChatService;
import edu.java.scrapper.service.jooq.JOOQLinkService;
import edu.java.scrapper.service.jooq.JOOQTgChatService;
import edu.java.scrapper.utils.linkverifier.LinkVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LinkServiceTest extends IntegrationEnvironment{
    private static final Map<String, ChatRepository> chatRepositories = new HashMap<>();
    private static final Map<String, LinkRepository> linkRepositories = new HashMap<>();
    private static final Map<String, ChatLinkRepository> chatLinkRepositories = new HashMap<>();
    private static final Map<String, TgChatService> tgChatServiceMap = new HashMap<>();
    private static final Map<String, LinkService> linkServiceHashMap = new HashMap<>();
    @Autowired
    private JOOQChatRepository jooqChatRepository;

    @Autowired
    private JDBCChatRepository jdbcChatRepository;

    @Autowired
    private JOOQLinkRepository jooqLinkRepository;

    @Autowired
    private JDBCLinkRepository jdbcLinkRepository;

    @Autowired
    private JOOQChatLinkRepository jooqChatLinkRepository;

    @Autowired
    private JDBCChatLinkRepository jdbcChatLinkRepository;

    @Autowired
    private JDBCTgChatService jdbcTgChatService;

    @Autowired
    private JOOQTgChatService jooqTgChatService;

    @Autowired
    private JDBCLinkService jdbcLinkService;

    @Autowired
    private JOOQLinkService jooqLinkService;

    @MockBean
    private LinkVerifier linkVerifier;

    @BeforeAll
    void setup() {
        chatRepositories.put("JOOQ", jooqChatRepository);
        chatRepositories.put("JDBC", jdbcChatRepository);
        linkRepositories.put("JOOQ", jooqLinkRepository);
        linkRepositories.put("JDBC", jdbcLinkRepository);
        chatLinkRepositories.put("JOOQ", jooqChatLinkRepository);
        chatLinkRepositories.put("JDBC", jdbcChatLinkRepository);
        tgChatServiceMap.put("JOOQ", jooqTgChatService);
        tgChatServiceMap.put("JDBC", jdbcTgChatService);
        linkServiceHashMap.put("JOOQ", jooqLinkService);
        linkServiceHashMap.put("JDBC", jdbcLinkService);
    }

    Stream<Arguments> implementations() {
        return Stream.of(
            Arguments.of("JOOQ", chatRepositories.get("JOOQ"),
                linkRepositories.get("JOOQ"),
                chatLinkRepositories.get("JOOQ"),
                tgChatServiceMap.get("JOOQ"),
                linkServiceHashMap.get("JOOQ")),
            Arguments.of("JDBC",
                chatRepositories.get("JDBC"),
                linkRepositories.get("JDBC"),
                chatLinkRepositories.get("JDBC"),
                tgChatServiceMap.get("JDBC"),
                linkServiceHashMap.get("JDBC"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Добавление ссылки в чат")
    @Transactional
    @Rollback
    public void testAdd(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId = 123L;
        URI uri = URI.create("http://example.com");

        tgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri)).thenReturn(true);
        Link addedLink = linkService.add(tgChatId, uri);

        Assertions.assertTrue(linkRepository.getLinkByUri(uri).isPresent());
        Assertions.assertTrue(chatLinkRepository.exists(tgChatId, addedLink.getId()));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Удаление ссылки из чата по ссылке")
    @Transactional
    @Rollback
    public void testRemoveByUri(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {
        long tgChatId = 123L;
        URI uri = URI.create("http://example.com");

        tgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri)).thenReturn(true);
        Link addedLink = linkService.add(tgChatId, uri);

        Assertions.assertTrue(linkRepository.getLinkByUri(uri).isPresent());
        Assertions.assertTrue(chatLinkRepository.exists(tgChatId, addedLink.getId()));

        linkService.remove(tgChatId, addedLink.getUri());
        Assertions.assertFalse(chatLinkRepository.exists(tgChatId, addedLink.getId()));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Удаление ссылки из чата по индефикатору")
    @Transactional
    @Rollback
    public void testRemoveById(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId = 123L;
        URI uri = URI.create("http://example.com");

        tgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri)).thenReturn(true);
        Link addedLink = linkService.add(tgChatId, uri);
        long linkId = addedLink.getId();

        Assertions.assertTrue(linkRepository.getLinkById(linkId).isPresent());
        Assertions.assertTrue(chatLinkRepository.exists(tgChatId, addedLink.getId()));

        linkService.remove(tgChatId, linkId);
        Assertions.assertFalse(chatLinkRepository.exists(tgChatId, linkId));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение всех ссылок чата")
    @Transactional
    @Rollback
    public void testListAll(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId = 123L;
        URI uri1 = URI.create("http://example1.com");
        URI uri2 = URI.create("http://example2.com");

        tgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri1)).thenReturn(true);
        when(linkVerifier.checkLink(uri2)).thenReturn(true);
        Link addedLink1 = linkService.add(tgChatId, uri1);
        Link addedLink2 = linkService.add(tgChatId, uri2);

        Collection<Link> links = linkService.listAll(tgChatId);

        Assertions.assertEquals(2, links.size());
        Assertions.assertTrue(links.contains(addedLink1));
        Assertions.assertTrue(links.contains(addedLink2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Очистка неиспользуемых ссылок")
    @Transactional
    @Rollback
    public void testCleanupUnusedLinks(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId = 123L;
        URI uri1 = URI.create("http://example1.com");
        URI uri2 = URI.create("http://example2.com");

        tgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri1)).thenReturn(true);
        when(linkVerifier.checkLink(uri2)).thenReturn(true);
        Link addedLink1 = linkService.add(tgChatId, uri1);
        Link addedLink2 = linkService.add(tgChatId, uri2);

        Assertions.assertTrue(chatLinkRepository.exists(tgChatId, addedLink1.getId()));
        Assertions.assertTrue(chatLinkRepository.exists(tgChatId, addedLink2.getId()));

        tgChatService.unregister(tgChatId);

        Assertions.assertFalse(chatLinkRepository.exists(tgChatId, addedLink1.getId()));
        Assertions.assertFalse(chatLinkRepository.exists(tgChatId, addedLink2.getId()));

        Assertions.assertTrue(linkRepository.getLinkById(addedLink1.getId()).isPresent());
        Assertions.assertTrue(linkRepository.getLinkById(addedLink2.getId()).isPresent());

        int cleanupCount = linkService.cleanUpUnusedLink();

        Assertions.assertEquals(2, cleanupCount);

        Assertions.assertFalse(linkRepository.getLinkById(addedLink1.getId()).isPresent());
        Assertions.assertFalse(linkRepository.getLinkById(addedLink2.getId()).isPresent());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Поиск старейших ссылок")
    @Transactional
    @Rollback
    public void testFindOldestLinks(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId = 123L;
        URI uri1 = URI.create("http://example1.com");
        URI uri2 = URI.create("http://example2.com");
        URI uri3 = URI.create("http://example3.com");

        tgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri1)).thenReturn(true);
        when(linkVerifier.checkLink(uri2)).thenReturn(true);
        when(linkVerifier.checkLink(uri3)).thenReturn(true);
        Link addedLink1 = linkService.add(tgChatId, uri1);
        Link addedLink2 = linkService.add(tgChatId, uri2);
        Link addedLink3 = linkService.add(tgChatId, uri3);

        int batchSize = 2;

        List<Link> oldestLinks = linkService.findOldestLinks(batchSize);


        Assertions.assertEquals(2, oldestLinks.size());
        Assertions.assertTrue(oldestLinks.contains(addedLink1));
        Assertions.assertTrue(oldestLinks.contains(addedLink2));
        Assertions.assertFalse(oldestLinks.contains(addedLink3));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение идентификаторов чатов по идентификатору ссылки")
    @Transactional
    @Rollback
    public void testGetChatIdsByLinkId(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId1 = 123L;
        long tgChatId2 = 456L;
        long tgChatId3 = 789L;

        URI uri = URI.create("http://example.com");

        tgChatService.register(tgChatId1);
        tgChatService.register(tgChatId2);
        tgChatService.register(tgChatId3);

        when(linkVerifier.checkLink(uri)).thenReturn(true);
        Link addedLink1 = linkService.add(tgChatId1, uri);
        Link addedLink2 = linkService.add(tgChatId2, uri);
        Link addedLink3 = linkService.add(tgChatId3, uri);

        List<Long> chatIds = linkService.getChatIdsByLinkId(addedLink1.getId());

        Assertions.assertEquals(3, chatIds.size());
        Assertions.assertTrue(chatIds.contains(tgChatId1));
        Assertions.assertTrue(chatIds.contains(tgChatId2));
        Assertions.assertTrue(chatIds.contains(tgChatId3));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Обновление времени последнего обновления ссылки")
    @Transactional
    @Rollback
    public void testUpdateLastUpdatedTime(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId = 123L;
        URI uri = URI.create("http://example.com");

        tgChatService.register(tgChatId);

        when(linkVerifier.checkLink(uri)).thenReturn(true);
        Link addedLink = linkService.add(tgChatId, uri);
        OffsetDateTime timeBefore = addedLink.getLastUpdatedAt();

        linkService.updateLastUpdatedTime(addedLink.getId());

        Link updateLink = linkRepository.getLinkById(addedLink.getId()).get();
        OffsetDateTime timeAfter = updateLink.getLastUpdatedAt();

        Assertions.assertTrue(timeBefore.isBefore(timeAfter));
    }
}
