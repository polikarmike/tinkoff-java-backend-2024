package edu.java.scrapper.dto.stackoverflow;

import edu.java.scrapper.exception.InvalidLinkException;
import lombok.Getter;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SOQuestLink {
    @Getter private String questionId;
    private final Pattern pattern = Pattern.compile("/questions/(\\d+)");

    public SOQuestLink(URI uri) {
        parseStackOverflowQuestionUri(uri);
    }

    private void parseStackOverflowQuestionUri(URI uri) {
        String path = uri.getPath();
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            questionId = matcher.group(1);
        } else {
            throw new InvalidLinkException("Invalid Stack Overflow question URI: " + uri);
        }
    }

    public String toString() {
        return "SOQuestLink{questionId='" + questionId + "'}";
    }
}
