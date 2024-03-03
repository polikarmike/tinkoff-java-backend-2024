package edu.java.bot.client.scrapper;


import edu.java.common.dto.responses.ListLinksResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;

public interface ScrapperClient {
    ListLinksResponse getAllLinks(long tgChatId);

    Void addLink(long tgChatId, URI link);

    Void removeLink(long tgChatId, URI link);

    ResponseEntity<String> registerChat(Long id);

    ResponseEntity<String> deleteChat(Long id);
}
