package edu.java.client.github;

import edu.java.dto.GHRepoResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubWebClient implements GitHubClient {
    private static final String DEFAULT_BASE_URL = "https://api.github.com/";
    private static final String REPO_ENDPOINT_TEMPLATE = "/repos/{owner}/{repo}";
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
}
