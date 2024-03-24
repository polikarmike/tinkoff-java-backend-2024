package edu.java.scrapper.service.jooq;

import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.dto.entity.jooq_jdbc.Chat;
import edu.java.scrapper.exception.MissingChatException;
import edu.java.scrapper.exception.RepeatedRegistrationException;
import edu.java.scrapper.service.TgChatService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JOOQTgChatService implements TgChatService {
    private final ChatRepository chatRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final JOOQLinkService linkService;
    private static final String MISSING_CHAT_ERROR_MESSAGE = "Chat does not exist";
    private static final String REPEATED_CHAT_ADDITION_ERROR_MESSAGE = "Chat already exists";


    public void register(long tgChatId) {
        Optional<Chat> existingChat = chatRepository.getById(tgChatId);
        if (existingChat.isPresent()) {
            throw new RepeatedRegistrationException(REPEATED_CHAT_ADDITION_ERROR_MESSAGE);
        }

        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(LocalDateTime.now().atOffset(ZoneOffset.UTC));

        chatRepository.add(chat);
    }


    public void unregister(long tgChatId) {
        Chat existingChat = chatRepository.getById(tgChatId)
            .orElseThrow(() -> new  MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        long chatId = existingChat.getId();
        List<Long> linksIds = chatLinkRepository.getLinkIdsByChatId(chatId);

        linksIds.forEach(linkId -> linkService.remove(chatId, linkId));

        chatRepository.remove(tgChatId);
    }
}
