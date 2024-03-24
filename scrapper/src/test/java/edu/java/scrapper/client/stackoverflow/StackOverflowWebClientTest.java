package edu.java.scrapper.client.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.scrapper.dto.stackoverflow.SQQuestAnswerResponse;
import edu.java.scrapper.dto.stackoverflow.SOQuestResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class StackOverflowWebClientTest {
    private static final int WIREMOCK_PORT = 8080;
    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(WIREMOCK_PORT));
        wireMockServer.start();
        WireMock.configureFor("localhost", WIREMOCK_PORT);
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("Тестирование получения вопроса")
    public void fetchQuestionTest(){
        // given
        String jsonResponse = """
            {
              "items": [
                {
                  "question_id": 123456,
                  "link": "https://stackoverflow.com/questions/123456",
                  "last_activity_date": "1708870512"
                }
              ]
            }
            """;

        stubFor(get(urlEqualTo("/questions/78056268?site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)));

        StackOverflowClient gitHubClient = new StackOverflowWebClient("http://localhost:" + WIREMOCK_PORT);

        // when
        SOQuestResponse response = gitHubClient.fetchQuestion("78056268");

        // then
        assertNotNull(response);

        SOQuestResponse.ItemResponse itemResponse = response.items().get(0);

        assertEquals("123456", itemResponse.id().toString());

        assertEquals("https://stackoverflow.com/questions/123456", itemResponse.url());

        OffsetDateTime expectedLastActivityDate = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1708870512), ZoneOffset.UTC);

        assertEquals(expectedLastActivityDate, itemResponse.lastUpdateTime());
    }

    @Test
    @DisplayName("Тестирование получения ответа на вопроса")
    public void fetchAnswerTest(){
        // given
        String jsonResponse = """
            {
              "items": [
                {
                  "last_activity_date": "1708870512"
                },
                {
                  "last_activity_date": "1538230242"
                }
              ]
            }
            """;

        stubFor(get(urlEqualTo("/questions/78056268/answers?site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)));

        StackOverflowClient gitHubClient = new StackOverflowWebClient("http://localhost:" + WIREMOCK_PORT);

        // when
        SQQuestAnswerResponse response = gitHubClient.fetchAnswers("78056268");

        // then
        assertNotNull(response);

        SQQuestAnswerResponse.ItemResponse itemResponse1 = response.items().get(0);
        SQQuestAnswerResponse.ItemResponse itemResponse2 = response.items().get(1);

        OffsetDateTime expectedLastActivityDate1 = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1708870512), ZoneOffset.UTC);
        OffsetDateTime expectedLastActivityDate2 = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1538230242), ZoneOffset.UTC);

        assertEquals(expectedLastActivityDate1, itemResponse1.lastUpdateTime());
        assertEquals(expectedLastActivityDate2, itemResponse2.lastUpdateTime());
    }
}
