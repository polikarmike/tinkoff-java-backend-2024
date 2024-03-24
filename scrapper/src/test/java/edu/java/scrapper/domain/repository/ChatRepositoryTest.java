package edu.java.scrapper.domain.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dto.entity.jooq_jdbc.Chat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatRepositoryTest extends IntegrationEnvironment {
    private static final Map<String, ChatRepository> chatRepositories = new HashMap<>();
    @Autowired
    @Qualifier("JOOQChatRepository")
    private ChatRepository jooqChatRepository;

    @Autowired
    @Qualifier("JDBCChatRepository")
    private ChatRepository jdbcChatRepository;

    @BeforeAll
    void setup() {
        chatRepositories.put("JOOQ", jooqChatRepository);
        chatRepositories.put("JDBC", jdbcChatRepository);
    }

    Stream<Arguments> implementations() {
        return Stream.of(
            Arguments.of("JOOQ", chatRepositories.get("JOOQ")),
            Arguments.of("JDBC", chatRepositories.get("JDBC"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Добавление чата")
    @Transactional
    @Rollback
    void addTest(String implName, ChatRepository chatRepository) {
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));

        Chat existingChat = chatRepository.add(chat);

        assertEquals(chat.getId(), existingChat.getId());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Удаление чата")
    @Transactional
    @Rollback
    void removeTest(String implName, ChatRepository chatRepository) {
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(chat);

        Long id = chat.getId();

        Chat deletedChat = chatRepository.remove(id);

        assertEquals(chat.getId(), deletedChat.getId());


    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение чата по ID")
    @Transactional
    @Rollback
    void getByIdTest(String implName, ChatRepository chatRepository) {
        Chat chat = new Chat();
        chat.setId(12345L);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(chat);

        Long id = chat.getId();

        Optional<Chat> retrievedChat = chatRepository.getById(id);

        assertNotNull(retrievedChat.orElse(null));
        assertEquals(id, retrievedChat.get().getId());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение всех чатов")
    @Transactional
    @Rollback
    void findAllTest(String implName, ChatRepository chatRepository) {
        Chat chat1 = new Chat();
        chat1.setId(1L);
        chat1.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(chat1);

        Chat chat2 = new Chat();
        chat2.setId(2L);
        chat2.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(chat2);

        List<Chat> chats = chatRepository.findAll();

        assertEquals(2, chats.size());

        assertTrue(chats.stream().anyMatch(chat -> chat.getId().equals(chat1.getId())));
        assertTrue(chats.stream().anyMatch(chat -> chat.getId().equals(chat2.getId())));
    }
}
