package edu.java.client.stackoverflow;

import edu.java.dto.SOQuestResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClientImpl implements StackOverflowClient {
    private static final String DEFAULT_BASE_URL = "https://api.stackexchange.com/2.3/";
    private static final String QUESTION_ENDPOINT_TEMPLATE = "/questions/{questionId}?site=stackoverflow";
    private final WebClient webClient;

    public StackOverflowClientImpl() {
        this.webClient = WebClient.builder()
            .baseUrl(DEFAULT_BASE_URL)
            .build();
    }

    public StackOverflowClientImpl(String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public SOQuestResponse fetchQuestion(String questionId) {
        return webClient.get()
            .uri(QUESTION_ENDPOINT_TEMPLATE, questionId)
            .retrieve()
            .bodyToMono(SOQuestResponse.class)
            .block();
    }
}
