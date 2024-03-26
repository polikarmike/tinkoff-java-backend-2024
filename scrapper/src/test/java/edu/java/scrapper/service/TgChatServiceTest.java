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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TgChatServiceTest extends IntegrationEnvironment {
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
    @DisplayName("Регистрация чата с помощью сервиса")
    @Transactional
    @Rollback
    void registerTest(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService) {

        long tgChatId = 123L;

        tgChatService.register(tgChatId);

        Assertions.assertTrue(chatRepository.getById(tgChatId).isPresent());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Удаление чата без ссылок")
    @Transactional
    @Rollback
    void uregisterTest_empty(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId = 123L;

        tgChatService.register(tgChatId);

        Assertions.assertTrue(chatRepository.getById(tgChatId).isPresent());

        tgChatService.unregister(tgChatId);

        Assertions.assertTrue(chatRepository.getById(tgChatId).isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Удаление чата со ссылками")
    @Transactional
    @Rollback
    void uregisterTest_notEmpty(String implName,
        ChatRepository chatRepository,
        LinkRepository linkRepository,
        ChatLinkRepository chatLinkRepository,
        TgChatService tgChatService,
        LinkService linkService) {

        long tgChatId = 123L;
        URI uri = URI.create("example.com");

        tgChatService.register(tgChatId);

        Assertions.assertTrue(chatRepository.getById(tgChatId).isPresent());

        when(linkVerifier.checkLink(uri)).thenReturn(Boolean.TRUE);
        Link link = linkService.add(tgChatId, uri);

        Assertions.assertTrue(chatLinkRepository.exists(tgChatId, link.getId()));

        tgChatService.unregister(tgChatId);

        Assertions.assertTrue(chatRepository.getById(tgChatId).isEmpty());
        Assertions.assertFalse(chatLinkRepository.exists(tgChatId, link.getId()));
    }
}

