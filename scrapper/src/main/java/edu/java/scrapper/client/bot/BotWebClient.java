package edu.java.scrapper.client.bot;

import edu.java.common.dto.requests.LinkUpdateRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class BotWebClient implements BotClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private final WebClient webClient;

    public BotWebClient() {
        this.webClient = WebClient.builder()
            .baseUrl(DEFAULT_BASE_URL)
            .build();
    }

    public BotWebClient(String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public String sendUpdate(LinkUpdateRequest updateRequest) {
        return webClient.post()
            .uri("/updates")
            .body(BodyInserters.fromValue(updateRequest))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
