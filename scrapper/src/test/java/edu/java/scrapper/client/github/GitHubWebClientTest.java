package edu.java.scrapper.client.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.scrapper.dto.GHRepoResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                  "id": 1296269,
                  "node_id": "MDEwOlJlcG9zaXRvcnkxMjk2MjY5",
                  "name": "Hello-World",
                  "full_name": "octocat/Hello-World",
                  "html_url": "https://github.com/octocat/Hello-World",
                  "visibility": "public",
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
        assertEquals(OffsetDateTime.parse("2011-01-26T19:06:43Z"), response.pushedAt());
        assertEquals(OffsetDateTime.parse("2011-01-26T19:01:12Z"), response.createdAt());
        assertEquals(OffsetDateTime.parse("2022-02-24T16:40:42Z"), response.updateAt());
    }
}
