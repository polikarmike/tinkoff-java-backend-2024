package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.client.scrapper.ScrapperWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ClientConfiguration {
    @Value("${clients.scrapper.host}")
    private String scrapperHost;

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperWebClient(scrapperHost);
    }
}
