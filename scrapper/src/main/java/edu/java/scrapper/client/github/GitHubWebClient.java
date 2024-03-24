package edu.java.scrapper.client.github;

import edu.java.scrapper.dto.github.GHEventResponse;
import edu.java.scrapper.dto.github.GHRepoResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubWebClient implements GitHubClient {
    private static final String DEFAULT_BASE_URL = "https://api.github.com/";
    private static final String REPO_ENDPOINT_TEMPLATE = "/repos/{owner}/{repo}";
    private static final String EVENTS_ENDPOINT = "/repos/{owner}/{repo}/events";
    private final WebClient webClient;

    public GitHubWebClient() {
        this.webClient = WebClient.builder()
            .baseUrl(DEFAULT_BASE_URL)
            .build();
    }

    public GitHubWebClient(String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public GHRepoResponse fetchRepository(String owner, String repoName) {
        return webClient.get()
            .uri(REPO_ENDPOINT_TEMPLATE, owner, repoName)
            .retrieve()
            .bodyToMono(GHRepoResponse.class)
            .block();
    }

    @Override
    public GHEventResponse fetchEvents(String owner, String repoName) {
        return webClient.get()
            .uri(EVENTS_ENDPOINT, owner, repoName)
            .retrieve()
            .bodyToMono(GHEventResponse.class)
            .block();
    }
}
