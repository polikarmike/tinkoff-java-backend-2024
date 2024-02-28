package edu.java.configuration;

import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubClientImpl;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowClientImpl;
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
        return new GitHubClientImpl(githubHost);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClientImpl(stackHost);
    }
}



