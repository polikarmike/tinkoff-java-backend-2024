package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dto.entity.Chat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@SpringBootTest
class JOOQChatRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JOOQChatRepository jooqChatRepository;

    @Test
    @DisplayName("Добавление чата")
    @Transactional
    @Rollback
    void addTest() {
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));

        jooqChatRepository.add(chat);
    }
}
