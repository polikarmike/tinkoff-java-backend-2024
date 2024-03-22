package edu.java.scrapper.utils.linkverifier.verifiers;

import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.dto.stackoverflow.SOQuestLink;
import edu.java.scrapper.dto.stackoverflow.SOQuestResponse;
import edu.java.scrapper.utils.linkverifier.WebLinkVerifier;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StackOverflowWebLinkVerifier implements WebLinkVerifier {
    private final StackOverflowClient stackOverflowClient;
    private static final String BASE_HOST = "stackoverflow.com";

    @Override
    public boolean isLinkValid(URI uri) {
        try {
            SOQuestLink questLink = new SOQuestLink(uri);
            String questionId = questLink.getQuestionId();
            SOQuestResponse response = stackOverflowClient.fetchQuestion(questionId);
            return response != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String getHost() {
        return BASE_HOST;
    }
}
