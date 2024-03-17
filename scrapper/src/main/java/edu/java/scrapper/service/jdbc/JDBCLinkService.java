package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.dao.repository.ChatDAO;
import edu.java.scrapper.dao.repository.ChatLinkDAO;
import edu.java.scrapper.dao.repository.LinkDAO;
import edu.java.scrapper.dto.entity.Chat;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.exception.LinkNotFoundException;
import edu.java.scrapper.exception.MissingChatException;
import edu.java.scrapper.exception.RepeatedLinkAdditionException;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.utils.LinkVerifier;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JDBCLinkService implements LinkService {
    private final LinkDAO linkRepository;
    private final ChatDAO chatRepository;
    private final ChatLinkDAO chatLinkRepository;
    private final LinkVerifier linkVerifier;
    private static final String MISSING_CHAT_ERROR_MESSAGE = "Chat does not exist";
    private static final String INVALID_LINK_ERROR_MESSAGE = "Invalid link: ";
    private static final String REPEATED_LINK_ADDITION_ERROR_MESSAGE = "Link already exists in the chat";
    private static final String NO_LINK_ERROR_MESSAGE = "Link does not exist";
    private static final String LINK_NOT_FOUND_ERROR_MESSAGE = "Link not found in the chat";

    @Override
    public Link add(long tgChatId, URI uri) {
        if (!linkVerifier.isLinkValid(uri)) {
            throw new InvalidLinkException(INVALID_LINK_ERROR_MESSAGE + uri.toString());
        }

        Chat chat = chatRepository.getById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        Link link = linkRepository.getLinkByUri(uri)
            .orElseGet(() -> linkRepository.add(uri));

        List<Long> existingLinkIds = chatLinkRepository.getLinkIdsByChatId(chat.getId());
        if (existingLinkIds.contains(link.getId())) {
            throw new RepeatedLinkAdditionException(REPEATED_LINK_ADDITION_ERROR_MESSAGE);
        }

        chatLinkRepository.add(chat.getId(), link.getId());
        return link;
    }


    @Override
    public Link remove(long tgChatId, URI url) {
        Chat chat = chatRepository.getById(tgChatId)
            .orElseThrow(() -> new MissingChatException(MISSING_CHAT_ERROR_MESSAGE));

        Link existingLink = linkRepository.getLinkByUri(url)
            .orElseThrow(() -> new LinkNotFoundException(NO_LINK_ERROR_MESSAGE));

        List<Long> existingLinkIds = chatLinkRepository.getLinkIdsByChatId(chat.getId());
        if (!existingLinkIds.contains(existingLink.getId())) {
            throw new LinkNotFoundException(LINK_NOT_FOUND_ERROR_MESSAGE);
        }

        chatLinkRepository.remove(chat.getId(), existingLink.getId());
        List<Long> remainingChatIds = chatLinkRepository.getChatIdsByLinkId(existingLink.getId());
        if (remainingChatIds.isEmpty()) {
            linkRepository.remove(existingLink.getUri());
        }

        return existingLink;
    }

    @Override
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
