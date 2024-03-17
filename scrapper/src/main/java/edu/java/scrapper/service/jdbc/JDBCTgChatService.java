package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.repository.jdbc.JDBCChatRepository;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.exception.MissingChatException;
import edu.java.scrapper.exception.RepeatedRegistrationException;
import edu.java.scrapper.service.TgChatService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JDBCTgChatService implements TgChatService {
    private final JDBCChatRepository jdbcChatRepository;
    private static final String MISSING_CHAT_ERROR_MESSAGE = "Chat does not exist";
    private static final String REPEATED_CHAT_ADDITION_ERROR_MESSAGE = "Chat already exists";

    @Override
    public void register(long tgChatId) {
        Optional<Chat> existingChat = jdbcChatRepository.getById(tgChatId);
        if (existingChat.isPresent()) {
            throw new RepeatedRegistrationException(REPEATED_CHAT_ADDITION_ERROR_MESSAGE);
        }

        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));

        jdbcChatRepository.add(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        Optional<Chat> existingChat = jdbcChatRepository.getById(tgChatId);
        if (existingChat.isEmpty()) {
            throw new MissingChatException(MISSING_CHAT_ERROR_MESSAGE);
        }

        jdbcChatRepository.remove(tgChatId);
    }
}
