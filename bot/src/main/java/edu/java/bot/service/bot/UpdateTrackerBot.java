package edu.java.bot.service.bot;

import edu.java.bot.service.commands.impl.HelpCommand;
import edu.java.bot.service.listener.UpdateListener;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UpdateTrackerBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private final UpdateListener updateListener;

    @Autowired
    public UpdateTrackerBot(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    @PostConstruct
    public void start() {

        updateListener.startListening();
        LOGGER.info("Update listening have been started successfully");
    }
}
