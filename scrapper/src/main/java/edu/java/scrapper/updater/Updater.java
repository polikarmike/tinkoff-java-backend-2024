package edu.java.scrapper.updater;

import edu.java.scrapper.dto.entity.Link;

public interface Updater {
    String getUpdateMessage(Link link);

    String getHost();
}
