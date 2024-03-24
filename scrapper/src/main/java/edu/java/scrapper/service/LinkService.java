package edu.java.scrapper.service;

import edu.java.scrapper.dto.entity.LinkEntity;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    LinkEntity add(long tgChatId, URI url);

    LinkEntity remove(long tgChatId, URI url);

    Collection<? extends LinkEntity> listAll(long tgChatId);

}
