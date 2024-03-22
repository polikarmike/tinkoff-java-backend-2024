package edu.java.scrapper.service;

import edu.java.scrapper.domain.repository.ChatRepository;
import edu.java.scrapper.domain.repository.ChatLinkRepository;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.MissingChatException;
import edu.java.scrapper.exception.RepeatedLinkAdditionException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import edu.java.scrapper.utils.linkverifier.LinkVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkVerifier linkVerifier;
    private static final String MISSING_CHAT_ERROR_MESSAGE = "Chat does not exist";
    private static final String INVALID_LINK_ERROR_MESSAGE = "Invalid link: ";
    private static final String REPEATED_LINK_ADDITION_ERROR_MESSAGE = "Link already exists in the chat";
    private static final String NO_LINK_ERROR_MESSAGE = "Link does not exist";
    private static final String LINK_NOT_FOUND_ERROR_MESSAGE = "Link not found in the chat";


    public Link add(long tgChatId, URI uri) {
        if (!linkVerifier.checkLink(uri)) {
            throw new InvalidLinkException(INVALID_LINK_ERROR_MESSAGE + uri);
        }

        Chat chat = chatRepository.getById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        Link link = linkRepository.getLinkByUri(uri)
            .orElseGet(() -> linkRepository.add(uri));

        long chatId = chat.getId();
        long linkId = link.getId();

        if (chatLinkRepository.exists(chatId,linkId)) {
            throw new RepeatedLinkAdditionException(REPEATED_LINK_ADDITION_ERROR_MESSAGE);
        }

        chatLinkRepository.add(chatId, linkId);
        return link;
    }



    public Link remove(long tgChatId, URI url) {
        Chat chat = chatRepository.getById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        Link existingLink = linkRepository.getLinkByUri(url)
            .orElseThrow(() -> new LinkNotFoundException(NO_LINK_ERROR_MESSAGE));

        long chatId = chat.getId();
        long linkId = existingLink.getId();

        if (chatLinkRepository.exists(chatId,linkId)) {
            throw new LinkNotFoundException(LINK_NOT_FOUND_ERROR_MESSAGE);
        }

        chatLinkRepository.remove(chatId, linkId);
        List<Long> remainingChatIds = chatLinkRepository.getChatIdsByLinkId(existingLink.getId());
        if (remainingChatIds.isEmpty()) {
            linkRepository.remove(linkId);
        }

        return existingLink;
    }

    public Link remove(long tgChatId, long id) {
        Chat chat = chatRepository.getById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        Link existingLink = linkRepository.getLinkById(id)
            .orElseThrow(() -> new LinkNotFoundException(NO_LINK_ERROR_MESSAGE));

        long chatId = chat.getId();
        long linkId = existingLink.getId();

        if (chatLinkRepository.exists(chatId,linkId)) {
            throw new LinkNotFoundException(LINK_NOT_FOUND_ERROR_MESSAGE);
        }

        chatLinkRepository.remove(chatId, linkId);
        List<Long> remainingChatIds = chatLinkRepository.getChatIdsByLinkId(existingLink.getId());
        if (remainingChatIds.isEmpty()) {
            linkRepository.remove(linkId);
        }

        return existingLink;
    }

    public Collection<Link> listAll(long tgChatId) {
        Chat chat = chatRepository.getById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        List<Long> linkIds = chatLinkRepository.getLinkIdsByChatId(chat.getId());
        Collection<Link> links = new ArrayList<>();

        for (Long linkId : linkIds) {
            Optional<Link> linkOptional = linkRepository.getLinkById(linkId);
            linkOptional.ifPresent(links::add);
        }

        return links;
    }
}
