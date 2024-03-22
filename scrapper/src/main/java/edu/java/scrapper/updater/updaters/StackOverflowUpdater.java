package edu.java.scrapper.updater.updaters;

import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.dto.stackoverflow.SOQuestLink;
import edu.java.scrapper.dto.stackoverflow.SOQuestResponse;
import edu.java.scrapper.dto.stackoverflow.SQQuestAnswerResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackOverflowUpdater {
    private final StackOverflowClient stackOverflowClient;
    private static final String BASE_HOST = "stackoverflow.com";

    public String processStackOverflowLink(Link link) {
        URI uri = link.getUri();
        SOQuestLink soQuestion = new SOQuestLink(uri);
        String questionId = soQuestion.getQuestionId();

        SOQuestResponse soQuestResponse = stackOverflowClient.fetchQuestion(questionId);

        int newAnswers = 0;

        for (var itemQuestionResponse : soQuestResponse.items()) {
            if (itemQuestionResponse.lastUpdateTime().isAfter(link.getLastUpdatedAt())) {
                SQQuestAnswerResponse sqQuestAnswerResponse =
                    stackOverflowClient.fetchAnswers(questionId);

                for (var itemAnswerResponse : sqQuestAnswerResponse.items()) {
                    if (itemAnswerResponse.lastUpdateTime().isAfter(link.getLastUpdatedAt())) {
                        newAnswers++;
                    }
                }
            }
        }

        return (newAnswers > 0) ? "Количество новых ответов: " + newAnswers : null;
    }

    public String getHost() {
        return BASE_HOST;
    }
}
