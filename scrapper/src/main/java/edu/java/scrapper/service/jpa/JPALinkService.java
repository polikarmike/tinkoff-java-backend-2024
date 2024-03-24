package edu.java.scrapper.service.jpa;

import edu.java.scrapper.domain.repository.jpa.JPAChatRepository;
import edu.java.scrapper.domain.repository.jpa.JPALinkRepository;
import edu.java.scrapper.dto.entity.jpa.Chat;
import edu.java.scrapper.dto.entity.jpa.Link;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.MissingChatException;
import edu.java.scrapper.exception.RepeatedLinkAdditionException;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.utils.linkverifier.LinkVerifier;
import java.net.URI;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JPALinkService implements LinkService {
    private final JPALinkRepository linkRepository;
    private final JPAChatRepository chatRepository;
    private final LinkVerifier linkVerifier;
    private static final String MISSING_CHAT_ERROR_MESSAGE = "Chat does not exist";
    private static final String INVALID_LINK_ERROR_MESSAGE = "Invalid link: ";
    private static final String REPEATED_LINK_ADDITION_ERROR_MESSAGE = "Link already exists in the chat";
    private static final String NO_LINK_ERROR_MESSAGE = "Link does not exist";
    private static final String LINK_NOT_FOUND_ERROR_MESSAGE = "Link not found in the chat";

    @Override
    @Transactional
    public Link add(long tgChatId, URI uri) {
        if (!linkVerifier.checkLink(uri)) {
            throw new InvalidLinkException(INVALID_LINK_ERROR_MESSAGE + uri);
        }

        Chat chat = chatRepository.findById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));


        Link link = getOrCreate(uri);

        if (linkRepository.exists(chat.getId(), link.getId())) {
            throw new RepeatedLinkAdditionException(REPEATED_LINK_ADDITION_ERROR_MESSAGE);
        }

        chat.getLinks().add(link);

        chatRepository.save(chat);

        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI uri) {
        Chat chat = chatRepository.findById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        Link existingLink = linkRepository.findByUri(uri)
            .orElseThrow(() -> new LinkNotFoundException(NO_LINK_ERROR_MESSAGE));

        if (!linkRepository.exists(chat.getId(), existingLink.getId())) {
            throw new LinkNotFoundException(LINK_NOT_FOUND_ERROR_MESSAGE);
        }

        chat.getLinks().remove(existingLink);
        chatRepository.save(chat);

        existingLink.getChats().remove(chat);
        linkRepository.save(existingLink);

        if (existingLink.getChats().isEmpty()) {
            linkRepository.delete(existingLink);
        }

        return existingLink;
    }

    @Override
    public Collection<Link> listAll(long tgChatId) {
        Chat chat = chatRepository.findById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        return chat.getLinks();
    }

    @Transactional
    private Link getOrCreate(URI uri) {
        return linkRepository.findByUri(uri)
            .orElseGet(() -> linkRepository.save(new Link(uri)));
    }
}
