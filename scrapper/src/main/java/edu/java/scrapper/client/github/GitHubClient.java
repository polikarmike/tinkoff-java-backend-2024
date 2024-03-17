package edu.java.scrapper.client.github;

import edu.java.scrapper.dto.github.GHEventResponse;
import edu.java.scrapper.dto.github.GHRepoResponse;

public interface GitHubClient {
    GHRepoResponse fetchRepository(String owner, String repoName);

    GHEventResponse fetchEvents(String owner, String repoName);
}
