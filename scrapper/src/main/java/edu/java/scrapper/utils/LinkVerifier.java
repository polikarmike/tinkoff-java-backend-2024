package edu.java.scrapper.utils;

import edu.java.scrapper.client.github.GitHubClient;
import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.dto.GHRepoResponse;
import edu.java.scrapper.dto.SOQuestResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LinkVerifier {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;


    public boolean isLinkValid(URI uri) {
        try {
            String url = uri.toString();
            if (url.contains("github.com")) {
                return isGitHubRepoValid(url);
            } else if (url.contains("stackoverflow.com")) {
                return isStackOverflowQuestionValid(url);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    private boolean isGitHubRepoValid(String url) {
        String[] parts = url.split("/");
        if (parts.length >= 2) {
            String owner = parts[parts.length - 2];
            String repoName = parts[parts.length - 1];
            GHRepoResponse response = gitHubClient.fetchRepository(owner, repoName);
            return response != null;
        }
        return false;
    }

    private boolean isStackOverflowQuestionValid(String url) {
        String[] parts = url.split("/");
        if (parts.length >= 1) {
            String questionId = parts[parts.length - 1];
            SOQuestResponse response = stackOverflowClient.fetchQuestion(questionId);
            return response != null;
        }
        return false;
    }
}
