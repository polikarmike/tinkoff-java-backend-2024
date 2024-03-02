package edu.java.configuration;

import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubWebClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ClientConfiguration {
    @Value("${clients.github.host}")
    private String githubHost;

    @Value("${clients.stack.host}")
    private String stackHost;

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubWebClient(githubHost);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowWebClient(stackHost);
    }
}



