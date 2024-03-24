package edu.java.scrapper.updater.updaters;

import edu.java.scrapper.client.github.GitHubClient;
import edu.java.scrapper.dto.entity.LinkEntity;
import edu.java.scrapper.dto.github.GHEventResponse;
import edu.java.scrapper.dto.github.GHRepoResponse;
import edu.java.scrapper.dto.github.GitHubRepoLink;
import edu.java.scrapper.updater.Updater;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GitHubUpdater implements Updater {
    private final GitHubClient gitHubClient;
    private static final String BASE_HOST = "github.com";

    @Override
    public String getUpdateMessage(LinkEntity link) {
        URI uri = link.getUri();
        GitHubRepoLink gitHubRepo = new GitHubRepoLink(uri);
        String owner = gitHubRepo.getOwner();
        String repoName = gitHubRepo.getRepoName();

        GHRepoResponse ghRepoResponse = gitHubClient.fetchRepository(owner, repoName);

        if (ghRepoResponse.lastActivityDate().isAfter(link.getLastUpdatedAt())) {
            GHEventResponse ghEventResponse = gitHubClient.fetchEvents(owner, repoName);
            if (ghEventResponse.createdAt().isAfter(link.getLastUpdatedAt())) {
                return processGitHubEvent(ghEventResponse);
            }
        }

        return null;
    }

    private String processGitHubEvent(GHEventResponse ghEventResponse) {
        String eventType = ghEventResponse.type();
        String message = null;
        if (Objects.equals(eventType, "PULL_REQUEST")) {
            message = "Новый Pull Request: " + ghEventResponse.payload().pullRequest().title();
        } else if (Objects.equals(eventType, "ISSUE")) {
            message = "Новый Issue: " + ghEventResponse.payload().issue().title();
        } else {
            message = "Появилось обновление";
        }

        return message;
    }

    public String getHost() {
        return BASE_HOST;
    }
}
