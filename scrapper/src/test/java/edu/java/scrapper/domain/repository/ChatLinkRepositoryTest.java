package edu.java.scrapper.domain.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatLinkRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatRepository;
import edu.java.scrapper.domain.repository.jdbc.JDBCLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQChatLinkRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQChatRepository;
import edu.java.scrapper.domain.repository.jooq.JOOQLinkRepository;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.dto.entity.Link;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatLinkRepositoryTest extends IntegrationEnvironment {

    private static final Map<String, ChatRepository> chatRepositories = new HashMap<>();
    private static final Map<String, LinkRepository> linkRepositories = new HashMap<>();
    private static final Map<String, ChatLinkRepository> chatLinkRepositories = new HashMap<>();
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

    @BeforeAll
    void setup() {
        chatRepositories.put("JOOQ", jooqChatRepository);
        chatRepositories.put("JDBC", jdbcChatRepository);
        linkRepositories.put("JOOQ", jooqLinkRepository);
        linkRepositories.put("JDBC", jdbcLinkRepository);
        chatLinkRepositories.put("JOOQ", jooqChatLinkRepository);
        chatLinkRepositories.put("JDBC", jdbcChatLinkRepository);
    }

    Stream<Arguments> implementations() {
        return Stream.of(
            Arguments.of("JOOQ", chatRepositories.get("JOOQ"), linkRepositories.get("JOOQ"), chatLinkRepositories.get("JOOQ")),
            Arguments.of("JDBC", chatRepositories.get("JDBC"), linkRepositories.get("JDBC"), chatLinkRepositories.get("JDBC"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Добавление связи между чатом и ссылкой")
    @Transactional
    @Rollback
    void addTest(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository)
        throws URISyntaxException {

        Long chatId = 1L;

        Chat exampleChat = new Chat();
        exampleChat.setId(chatId);
        exampleChat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(exampleChat);

        URI exampleURI = new URI("https://example.com");
        linkRepository.add(exampleURI);
        Long linkId = linkRepository.getLinkByUri(exampleURI).get().getId();

        chatLinkRepository.add(chatId, linkId);

        List<Long> retrievedLinkIds = chatLinkRepository.getLinkIdsByChatId(1L);
        assertTrue(retrievedLinkIds.contains(linkId));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Удаление связи между чатом и ссылкой")
    @Transactional
    @Rollback
    void removeTest(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository)
        throws URISyntaxException {

        Long chatId = 1L;

        Chat exampleChat = new Chat();
        exampleChat.setId(chatId);
        exampleChat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(exampleChat);

        URI exampleURI = new URI("https://example.com");
        linkRepository.add(exampleURI);
        Long linkId = linkRepository.getLinkByUri(exampleURI).get().getId();

        chatLinkRepository.add(chatId, linkId);
        chatLinkRepository.remove(chatId, linkId);

        List<Long> retrievedLinkIds = chatLinkRepository.getLinkIdsByChatId(chatId);

        Assertions.assertFalse(retrievedLinkIds.contains(linkId));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение идентификаторов ссылок по идентификатору чата")
    @Transactional
    @Rollback
    void getLinkIdsByChatIdTest(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository)
        throws URISyntaxException {

        Long chatId = 1L;

        Chat exampleChat = new Chat();
        exampleChat.setId(chatId);
        exampleChat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(exampleChat);

        URI exampleURI1 = new URI("https://example.com");
        linkRepository.add(exampleURI1);
        Long linkId1 = linkRepository.getLinkByUri(exampleURI1).get().getId();

        URI exampleURI2 = new URI("https://example2.com");
        linkRepository.add(exampleURI2);
        Long linkId2 = linkRepository.getLinkByUri(exampleURI2).get().getId();

        chatLinkRepository.add(chatId, linkId1);
        chatLinkRepository.add(chatId, linkId2);

        List<Long> retrievedLinkIds = chatLinkRepository.getLinkIdsByChatId(chatId);

        Assertions.assertEquals(2, retrievedLinkIds.size());
        assertTrue(retrievedLinkIds.contains(linkId1));
        assertTrue(retrievedLinkIds.contains(linkId2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение идентификаторов чатов по идентификатору ссылки")
    @Transactional
    @Rollback
    void getChatIdsByLinkIdTest(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository)
        throws URISyntaxException {

        Long chatId1 = 1L;

        Chat exampleChat1 = new Chat();
        exampleChat1.setId(chatId1);
        exampleChat1.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(exampleChat1);

        Long chatId2 = 2L;

        Chat exampleChat2 = new Chat();
        exampleChat2.setId(chatId2);
        exampleChat2.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(exampleChat2);

        URI exampleURI = new URI("https://example.com");
        linkRepository.add(exampleURI);
        Long linkId = linkRepository.getLinkByUri(exampleURI).get().getId();

        chatLinkRepository.add(chatId1, linkId);
        chatLinkRepository.add(chatId2, linkId);

        List<Long> retrievedChatIds = chatLinkRepository.getChatIdsByLinkId(linkId);

        Assertions.assertEquals(2, retrievedChatIds.size());
        assertTrue(retrievedChatIds.contains(chatId1));
        assertTrue(retrievedChatIds.contains(chatId2));
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Проверка существования связных идентификатора чата и идентификатора ссылки")
    @Transactional
    @Rollback
    void existChatIdLinkId(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository)
        throws URISyntaxException {

        Long chatId = 1L;

        Chat exampleChat = new Chat();
        exampleChat.setId(chatId);
        exampleChat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        Chat addedChat = chatRepository.add(exampleChat);

        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);


        chatLinkRepository.add(addedChat.getId(), addedLink.getId());


        assertTrue(chatLinkRepository.exists(addedChat.getId(), addedLink.getId()));
    }
}
