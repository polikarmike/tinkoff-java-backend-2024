package edu.java.bot.client.scrapper;


import edu.java.common.dto.responses.ListLinksResponse;
import java.net.URI;


public interface ScrapperClient {
    ListLinksResponse getAllLinks(long tgChatId);

    void addLink(long tgChatId, URI link);

    void removeLink(long tgChatId, URI link);

    String registerChat(Long id);

    String deleteChat(Long id);
}
