package edu.java.scrapper.updater;

import edu.java.scrapper.client.stackoverflow.StackOverflowClient;
import edu.java.scrapper.dto.entity.jooq_jdbc.Link;
import edu.java.scrapper.dto.stackoverflow.SOQuestResponse;
import edu.java.scrapper.dto.stackoverflow.SQQuestAnswerResponse;
import edu.java.scrapper.updater.updaters.StackOverflowUpdater;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StackOverflowUpdaterTest {

    @Mock
    private StackOverflowClient stackOverflowClient;

    @InjectMocks
    private StackOverflowUpdater stackOverflowUpdater;

    @Test
    @DisplayName("Проверка StackOverflowUpdater")
    public void testProcessStackOverflowLink() {
        OffsetDateTime futureTime = OffsetDateTime.now().plusDays(1);

        List<SOQuestResponse.ItemResponse> itemQuestResponses = new ArrayList<>();
        SOQuestResponse.ItemResponse itemQuestResponse = new SOQuestResponse.ItemResponse(
            1L,
            "example.com",
            futureTime);

        itemQuestResponses.add(itemQuestResponse);
        SOQuestResponse questResponse = new SOQuestResponse(itemQuestResponses);
        when(stackOverflowClient.fetchQuestion(anyString())).thenReturn(questResponse);


        List<SQQuestAnswerResponse.ItemResponse> itemAnswerResponses = new ArrayList<>();
        SQQuestAnswerResponse.ItemResponse itemAnswerResponse = new SQQuestAnswerResponse.ItemResponse(futureTime);
        itemAnswerResponses.add(itemAnswerResponse);

        SQQuestAnswerResponse answerResponse = new SQQuestAnswerResponse(itemAnswerResponses);
        when(stackOverflowClient.fetchAnswers(anyString())).thenReturn(answerResponse);


        Link link = new Link();
        link.setUri(URI.create("https://stackoverflow.com/questions/123"));
        link.setLastUpdatedAt(OffsetDateTime.now());

        String message = stackOverflowUpdater.processStackOverflowLink(link);

        assertEquals("Количество новых ответов: 1", message);
        verify(stackOverflowClient, times(1)).fetchQuestion(anyString());
        verify(stackOverflowClient, times(1)).fetchAnswers(anyString());
    }
}
