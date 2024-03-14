package edu.java.scrapper.client.bot;

import edu.java.common.dto.requests.LinkUpdateRequest;

public interface BotClient {
    String sendUpdate(LinkUpdateRequest updateRequest);
}
