package edu.java.scrapper.utils.linkverifier;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.net.URI;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LinkVerifierTest {

    @Test
    @DisplayName("Проверка ссылки StackOverflow")
    public void testSOCheckLink() throws Exception {
        URI uri = new URI("http://stackoverflow.com/questions/123");
        WebLinkVerifier mockVerifier = Mockito.mock(WebLinkVerifier.class);
        when(mockVerifier.getHost()).thenReturn("stackoverflow.com");
        when(mockVerifier.isLinkValid(uri)).thenReturn(true);
        LinkVerifier linkVerifier = new LinkVerifier(Arrays.asList(mockVerifier));

        boolean result = linkVerifier.checkLink(uri);

        assertTrue(result);
        verify(mockVerifier, times(1)).isLinkValid(uri);
    }

    @Test
    @DisplayName("Проверка ссылки с неизвестным хостом")
    public void testCheckLinkWithUnknownHost() throws Exception {
        URI uri = new URI("http://unknown.com/questions/123");
        WebLinkVerifier mockVerifier = Mockito.mock(WebLinkVerifier.class);
        when(mockVerifier.getHost()).thenReturn("stackoverflow.com");
        LinkVerifier linkVerifier = new LinkVerifier(Arrays.asList(mockVerifier));

        boolean result = linkVerifier.checkLink(uri);


        assertFalse(result);
        verify(mockVerifier, never()).isLinkValid(uri);
    }

    @Test
    @DisplayName("Проверка ссылки с неизвестным хостом")
    public void testGHCheckLink() throws Exception {
        URI uri = new URI("http://github.com/user/repo");
        WebLinkVerifier mockVerifier = Mockito.mock(WebLinkVerifier.class);
        when(mockVerifier.getHost()).thenReturn("github.com");
        when(mockVerifier.isLinkValid(uri)).thenReturn(true);
        LinkVerifier linkVerifier = new LinkVerifier(Arrays.asList(mockVerifier));

        boolean result = linkVerifier.checkLink(uri);

        assertTrue(result);
        verify(mockVerifier, times(1)).isLinkValid(uri);
    }
}


