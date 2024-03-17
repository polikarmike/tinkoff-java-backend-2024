package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.dto.stackoverflow.SOQuestResponse;
import edu.java.scrapper.dto.stackoverflow.SQQuestAnswerResponse;

public interface StackOverflowClient {
    SOQuestResponse fetchQuestion(String questionId);

    SQQuestAnswerResponse fetchAnswers(String questionId);
}
