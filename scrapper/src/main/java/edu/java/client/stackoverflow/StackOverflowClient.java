package edu.java.client.stackoverflow;

import edu.java.dto.SOQuestResponse;

public interface StackOverflowClient {
    SOQuestResponse fetchQuestion(String questionId);
}
