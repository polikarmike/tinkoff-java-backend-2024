package edu.java.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class LinkUpdaterScheduler {
    private static final String FIXED_DELAY_EXPRESSION =
        "#{T(java.time.Duration).parse('PT${app.scheduler.interval}').toMillis()}";

    @Scheduled(fixedDelayString = FIXED_DELAY_EXPRESSION)
    public void update() {
        log.info("Update method in scheduler");
    }
}
