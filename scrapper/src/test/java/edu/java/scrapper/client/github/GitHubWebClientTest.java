package edu.java.scrapper.client.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.scrapper.dto.github.GHEventResponse;
import edu.java.scrapper.dto.github.GHRepoResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class GitHubWebClientTest {
    private static final int WIREMOCK_PORT = 8089;
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
    @DisplayName("Тестирование получения репозитория")
    public void fetchRepositoryTest() {
        // given
        String jsonResponse = """
                {
                  "name": "Hello-World",
                  "html_url": "https://github.com/octocat/Hello-World",
                  "pushed_at": "2011-01-26T19:06:43Z",
                  "created_at": "2011-01-26T19:01:12Z",
                  "updated_at": "2022-02-24T16:40:42Z"
                }
                """;

        stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)));

        GitHubClient gitHubClient = new GitHubWebClient("http://localhost:" + WIREMOCK_PORT);

        // when
        GHRepoResponse response = gitHubClient.fetchRepository("octocat", "Hello-World");

        // then
        assertNotNull(response);
        assertEquals("Hello-World", response.repoName());
        assertEquals("https://github.com/octocat/Hello-World", response.link());
        assertEquals(OffsetDateTime.parse("2011-01-26T19:06:43Z"), response.lastActivityDate());
        assertEquals(OffsetDateTime.parse("2011-01-26T19:01:12Z"), response.createdAt());
        assertEquals(OffsetDateTime.parse("2022-02-24T16:40:42Z"), response.updateAt());
    }

    @Test
    @DisplayName("Тестирование получения событий")
    public void fetchEventsTest() {
        // given
        String jsonResponse = """
            {
              "type": "PushEvent",
              "payload": {
                "issue": {
                  "title": "Issue title"
                },
                "pull_request": {
                  "title": "PR title"
                }
              },
              "created_at": "2022-02-24T16:40:42Z"
            }
            """;

        stubFor(get(urlEqualTo("/repos/octocat/Hello-World/events"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)));

        GitHubClient gitHubClient = new GitHubWebClient("http://localhost:" + WIREMOCK_PORT);

        // when
        GHEventResponse response = gitHubClient.fetchEvents("octocat", "Hello-World");

        // then
        assertNotNull(response);
        assertEquals("PushEvent", response.type());
        assertEquals("Issue title", response.payload().issue().title());
        assertEquals("PR title", response.payload().pullRequest().title());
        assertEquals(OffsetDateTime.parse("2022-02-24T16:40:42Z"), response.createdAt());
    }
}
