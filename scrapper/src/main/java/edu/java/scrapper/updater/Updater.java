package edu.java.scrapper.updater;

import edu.java.scrapper.dto.entity.LinkEntity;

public interface Updater {
    String getUpdateMessage(LinkEntity link);

    String getHost();
}
