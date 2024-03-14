package edu.java.scrapper.configuration;

import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.bot.BotWebClient;
import edu.java.scrapper.client.github.GitHubClient;
import edu.java.scrapper.client.github.GitHubWebClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ClientConfiguration {
    @Value("${clients.bot.host}")
    private String botHost;

    @Value("${clients.github.host}")
    private String githubHost;

    @Value("${clients.stack.host}")
    private String stackHost;

    @Bean
    public BotClient botClient() {
        return new BotWebClient(botHost);
    }

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubWebClient(githubHost);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowWebClient(stackHost);
    }
}



