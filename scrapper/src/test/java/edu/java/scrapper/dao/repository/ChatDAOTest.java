package edu.java.scrapper.dao.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dto.entity.Chat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ChatDAOTest extends IntegrationEnvironment {

    @Autowired
    private ChatDAO chatRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    @DisplayName("Добавление чата")
    @Transactional
    @Rollback
    void addTest() throws SQLException {
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));

        chatRepository.add(chat);

        assertEquals(1, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Chat WHERE id = ?", Long.class, chat.getId()));
    }

    @Test
    @DisplayName("Удаление чата")
    @Transactional
    @Rollback
    void removeTest() {
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(chat);

        Long id = chat.getId();

        assertEquals(1, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Chat WHERE id = ?", Long.class, id));

        chatRepository.remove(id);

        assertEquals(0, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Chat WHERE id = ?", Long.class, id));
    }

    @Test
    @DisplayName("Получение чата по ID")
    @Transactional
    @Rollback
    void getByIdTest() {
        Chat chat = new Chat();
        chat.setId(12345L);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));
        chatRepository.add(chat);

        Long id = chat.getId();

        Chat retrievedChat = chatRepository.getById(id).get();


        assertNotNull(retrievedChat);
        assertEquals(id, retrievedChat.getId());
    }

    @Test
    @DisplayName("Получение всех чатов")
    @Transactional
    @Rollback
    void findAllTest() {
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
