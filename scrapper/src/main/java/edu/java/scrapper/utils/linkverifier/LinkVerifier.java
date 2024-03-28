package edu.java.scrapper.utils.linkverifier;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class LinkVerifier {
    private final Map<String, WebLinkVerifier> linkVerifiers = new HashMap<>();

    public LinkVerifier(List<WebLinkVerifier> webLinkVerifierList) {
        for (WebLinkVerifier webLinkVerifier : webLinkVerifierList) {
            linkVerifiers.put(webLinkVerifier.getHost(), webLinkVerifier);
        }
    }

    public Optional<WebLinkVerifier> getLinkVerifierByHost(String host) {
        return Optional.ofNullable(linkVerifiers.get(host));
    }

    public boolean checkLink(URI uri) {
        String host = uri.getHost();
        Optional<WebLinkVerifier> verifier = getLinkVerifierByHost(host);
        return verifier.map(webLinkVerifier -> webLinkVerifier.isLinkValid(uri)).orElse(false);
    }
}
