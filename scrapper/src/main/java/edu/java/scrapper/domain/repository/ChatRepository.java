package edu.java.scrapper.domain.repository;

import edu.java.scrapper.dto.entity.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    Chat add(Chat chat);

    Chat remove(Long id);
    Optional<Chat> getById(Long id);

    List<Chat> findAll();
}
