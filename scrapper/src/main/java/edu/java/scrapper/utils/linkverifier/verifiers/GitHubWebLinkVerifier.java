package edu.java.scrapper.utils.linkverifier.verifiers;

import edu.java.scrapper.client.github.GitHubClient;
import edu.java.scrapper.dto.github.GHRepoResponse;
import edu.java.scrapper.dto.github.GitHubRepoLink;
import edu.java.scrapper.utils.linkverifier.WebLinkVerifier;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitHubWebLinkVerifier implements WebLinkVerifier {
    private final GitHubClient gitHubClient;
    private static final String BASE_HOST = "github.com";

    @Override
    public boolean isLinkValid(URI uri) {
        try {
            GitHubRepoLink repoLink = new GitHubRepoLink(uri);
            String owner = repoLink.getOwner();
            String repoName = repoLink.getRepoName();
            GHRepoResponse response = gitHubClient.fetchRepository(owner, repoName);
            return response != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String getHost() {
        return BASE_HOST;
    }
}
