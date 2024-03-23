package edu.java.scrapper.updater;

import edu.java.scrapper.client.github.GitHubClient;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.dto.github.GHEventResponse;
import edu.java.scrapper.dto.github.GHRepoResponse;
import edu.java.scrapper.updater.updaters.GitHubUpdater;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitHubUpdaterTest {
    @Mock
    private GitHubClient gitHubClient;

    @InjectMocks
    private GitHubUpdater gitHubUpdater;


    @Test
    @DisplayName("Проверка GitHubUpdater")
    public void testGetUpdateMessage() {
        OffsetDateTime futureTime = OffsetDateTime.now().plusDays(1);
        GHRepoResponse repoResponse = new GHRepoResponse(
            "exampleName",
            "example.com",
            futureTime,
            futureTime,
            futureTime);

        when(gitHubClient.fetchRepository(anyString(), anyString())).thenReturn(repoResponse);

        GHEventResponse eventResponse = new GHEventResponse("PULL_REQUEST",
            new GHEventResponse.Payload(new GHEventResponse.Payload.Issue(null),
                new GHEventResponse.Payload.PullRequest("Example Title")
            ), futureTime);

        when(gitHubClient.fetchEvents(anyString(), anyString())).thenReturn(eventResponse);


        Link link = new Link();
        link.setUri(URI.create("https://github.com/owner/repo"));
        link.setLastUpdatedAt(OffsetDateTime.now());

        String message = gitHubUpdater.getUpdateMessage(link);

        assertEquals("Новый Pull Request: Example Title", message);
        verify(gitHubClient, times(1)).fetchRepository(anyString(), anyString());
        verify(gitHubClient, times(1)).fetchEvents(anyString(), anyString());
    }
}
