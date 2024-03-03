package edu.java.scrapper.client.stackoverflow;

import edu.java.scrapper.dto.SOQuestResponse;

public interface StackOverflowClient {
    SOQuestResponse fetchQuestion(String questionId);
}
