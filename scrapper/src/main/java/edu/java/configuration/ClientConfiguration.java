package edu.java.configuration;

import edu.java.client.github.GitHubClient;
import edu.java.client.github.GitHubClientImpl;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowClientImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@AllArgsConstructor
public class ClientConfiguration {

    private final ApplicationConfig applicationConfig;

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClientImpl(applicationConfig.urls().baseUrls().gitHub());
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClientImpl(applicationConfig.urls().baseUrls().stackOverflow());
    }
}



