package edu.java.bot.client.scrapper;


import edu.java.common.dto.requests.AddLinkRequest;
import edu.java.common.dto.requests.RemoveLinkRequest;
import edu.java.common.dto.responses.ListLinksResponse;
import java.net.URI;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;


public class ScrapperWebClient implements ScrapperClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    public static final String LINKS_ENDPOINT = "/links";
    public static final String CHATS_ENDPOINT = "/chats/{id}";
    public static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private final WebClient webClient;

    public ScrapperWebClient() {
        this.webClient = WebClient.builder()
            .baseUrl(DEFAULT_BASE_URL)
            .build();
    }

    public ScrapperWebClient(String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public ListLinksResponse getAllLinks(long tgChatId) {
        return webClient.get()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    @Override
    public Void addLink(long tgChatId, URI link) {
        AddLinkRequest request = new AddLinkRequest(link);
        webClient.post()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
        return null;
    }

    @Override
    public Void removeLink(long tgChatId, URI link) {
        RemoveLinkRequest request = new RemoveLinkRequest(link);
        webClient.method(HttpMethod.DELETE)
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
        return null;
    }

    @Override
    public ResponseEntity<String> registerChat(Long id) {
        return webClient.post()
            .uri(CHATS_ENDPOINT, id)
            .retrieve()
            .toEntity(String.class)
            .block();
    }

    @Override
    public ResponseEntity<String> deleteChat(Long id) {
        return webClient.delete()
            .uri(CHATS_ENDPOINT, id)
            .retrieve()
            .toEntity(String.class)
            .block();
    }
}
