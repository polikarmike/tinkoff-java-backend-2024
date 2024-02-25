package edu.java.client.github;

import edu.java.dto.GHRepoResponse;

public interface GitHubClient {
    GHRepoResponse fetchRepository(String owner, String repoName);
}
