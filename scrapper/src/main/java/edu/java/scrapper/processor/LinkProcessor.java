package edu.java.scrapper.processor;

import edu.java.common.dto.requests.LinkUpdateRequest;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.github.GitHubClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.domain.repository.jdbc.JDBCChatLinkRepository;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.dto.github.GHEventResponse;
import edu.java.scrapper.dto.github.GHRepoResponse;
import edu.java.scrapper.dto.stackoverflow.SOQuestResponse;
import edu.java.scrapper.dto.stackoverflow.SQQuestAnswerResponse;
import edu.java.scrapper.exception.InvalidLinkException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkProcessor {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final JDBCChatLinkRepository jdbcChatLinkRepository;
    private final BotClient botClient;

    public void processLink(Link link) {
        String url = link.getUri().toString();
        try {
            if (url.contains("github.com")) {
                processGitHubLink(link);
            } else if (url.contains("stackoverflow.com")) {
                processStackOverflowLink(link);
            }
        } catch (Exception e) {
            log.error("Error processing link: " + url, e);
            throw new InvalidLinkException("Unsupported URL: " + url);
        }
    }

    private void processGitHubLink(Link link) {
        String url = link.getUri().toString();
        String[] parts = url.split("/");
        String owner = parts[parts.length - 2];
        String repoName = parts[parts.length - 1];
        GHRepoResponse ghRepoResponse = gitHubClient.fetchRepository(owner, repoName);
        if (ghRepoResponse.lastActivityDate().isAfter(link.getLastUpdatedAt())) {
            GHEventResponse ghEventResponse = gitHubClient.fetchEvents(owner, repoName);
            if (ghEventResponse.createdAt().isAfter(link.getLastUpdatedAt())) {
                processGitHubEvent(ghEventResponse, link);
            }
        }
    }

    private void processGitHubEvent(GHEventResponse ghEventResponse, Link link) {
        String eventType = ghEventResponse.type();
        String message = null;
        if (Objects.equals(eventType, "PULL_REQUEST")) {
            message = "Новый Pull Request: " + ghEventResponse.payload().pullRequest().title();
        } else if (Objects.equals(eventType, "ISSUE")) {
            message = "Новый Issue: " + ghEventResponse.payload().issue().title();
        }
        if (message != null) {
            sendUpdate(link, message);
        }
    }

    private void processStackOverflowLink(Link link) {
        String url = link.getUri().toString();
        String[] parts = url.split("/");
        String questionId = parts[parts.length - 1];
        log.info(questionId);
        SOQuestResponse soQuestResponse = stackOverflowClient.fetchQuestion(questionId);
        int newAnswers = countNewAnswers(soQuestResponse, link, questionId);
        if (newAnswers > 0) {
            String message = "Количество новых ответов: " + newAnswers;
            sendUpdate(link, message);
        }
    }

    private int countNewAnswers(SOQuestResponse soQuestResponse, Link link, String questionId) {
        int newAnswers = 0;
        for (var itemQuestionResponse : soQuestResponse.items()) {
            if (itemQuestionResponse.lastUpdateTime().isAfter(link.getLastUpdatedAt())) {
                SQQuestAnswerResponse sqQuestAnswerResponse = stackOverflowClient.fetchAnswers(questionId);
                for (var itemAnswerResponse : sqQuestAnswerResponse.items()) {
                    if (itemAnswerResponse.lastUpdateTime().isAfter(link.getLastUpdatedAt())) {
                        newAnswers++;
                    }
                }
            }
        }
        return newAnswers;
    }

    private void sendUpdate(Link link, String message) {
        List<Long> chatsIds = jdbcChatLinkRepository.getChatIdsByLinkId(link.getId());
        botClient.sendUpdate(new LinkUpdateRequest(link.getId(), link.getUri(), message, chatsIds));
    }
}

