package edu.java.scrapper.client.github;

import edu.java.scrapper.dto.GHRepoResponse;

public interface GitHubClient {
    GHRepoResponse fetchRepository(String owner, String repoName);
}
