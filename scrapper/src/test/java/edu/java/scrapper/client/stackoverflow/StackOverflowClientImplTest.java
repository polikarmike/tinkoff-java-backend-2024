package edu.java.scrapper.client.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowClientImpl;
import edu.java.dto.SOQuestResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class StackOverflowClientImplTest {
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
                "is_answered": false,
                "view_count": 1,
                "answer_count": 0,
                "score": 0,
                "last_activity_date": 1708870512,
                "creation_date": 1708870512,
                "question_id": 78056268,
                "content_license": "CC BY-SA 4.0",
                "link": "https://stackoverflow.com/questions/78056268/debugging-rust-lifetimes-how-to-check-which-scope-each-elided-lifetime-is-relat",
                "title": "Debugging rust lifetimes: how to check which scope each elided lifetime is related to?"
            }
            """;

        stubFor(get(urlEqualTo("/questions/78056268?site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)));

        StackOverflowClient gitHubClient = new StackOverflowClientImpl("http://localhost:" + WIREMOCK_PORT);

        // when
        SOQuestResponse response = gitHubClient.fetchQuestion("78056268");

        // then
        assertNotNull(response);
        assertEquals("78056268", response.questId());

        String expectedLink = "https://stackoverflow.com/questions/78056268/debugging-rust-lifetimes-how-to-check-which-scope-each-elided-lifetime-is-relat";
        assertEquals(expectedLink, response.url());

        String expectedTitle = "Debugging rust lifetimes: how to check which scope each elided lifetime is related to?";
        assertEquals(expectedTitle, response.title());

        OffsetDateTime expectedCreationDate = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1708870512), ZoneOffset.UTC);
        OffsetDateTime expectedLastActivityDate = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1708870512), ZoneOffset.UTC);


        assertEquals(expectedCreationDate, response.creationDate());
        assertEquals(expectedLastActivityDate, response.lastActivityDate());
    }
}
