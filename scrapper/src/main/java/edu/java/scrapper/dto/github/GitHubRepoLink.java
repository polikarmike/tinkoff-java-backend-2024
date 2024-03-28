package edu.java.scrapper.dto.github;

import edu.java.scrapper.exception.InvalidLinkException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;

public class GitHubRepoLink {
    @Getter
    private String owner;

    @Getter
    private String repoName;

    private final Pattern pattern = Pattern.compile("/([^/]+)/([^/]+)");

    public GitHubRepoLink(URI uri) {
        parseGitHubRepoUri(uri);
    }

    private void parseGitHubRepoUri(URI uri) {
        String path = uri.getPath();
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            owner = matcher.group(1);
            repoName = matcher.group(2);
        } else {
            throw new InvalidLinkException("Invalid GitHub repository URI: " + uri);
        }
    }

    public String toString() {
        return "GitHubRepoLink{owner='" + owner + "', repoName='" + repoName + "'}";
    }
}
