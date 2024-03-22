package edu.java.scrapper.utils.linkverifier;

import java.net.URI;

public interface WebLinkVerifier {
    boolean isLinkValid(URI uri);

    String getHost();
}
