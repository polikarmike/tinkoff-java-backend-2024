package edu.java.scrapper.processor;

import edu.java.common.dto.requests.LinkUpdateRequest;
import edu.java.scrapper.client.bot.BotClient;
import edu.java.scrapper.client.github.GitHubClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.dao.repository.ChatLinkDAO;
import edu.java.scrapper.dto.GHRepoResponse;
import edu.java.scrapper.dto.SOQuestResponse;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.exception.InvalidLinkException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkProcessor {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final ChatLinkDAO chatLinkRepository;
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
            sendUpdate(link);
        }
    }

    private void processStackOverflowLink(Link link) {
        String url = link.getUri().toString();
        String[] parts = url.split("/");
        String questionId = parts[parts.length - 1];
        SOQuestResponse soQuestResponse = stackOverflowClient.fetchQuestion(questionId);
        if (soQuestResponse.lastActivityDate().isAfter(link.getLastUpdatedAt())) {
            sendUpdate(link);
        }
    }

    private void sendUpdate(Link link) {
        List<Long> chatsIds = chatLinkRepository.getChatIdsByLinkId(link.getId());
        botClient.sendUpdate(new LinkUpdateRequest(link.getId(), link.getUri(), "Появилось обновление", chatsIds));
    }
}

