package edu.java.scrapper.updater;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UpdaterHolder {
    private final Map<String, Updater> updaters = new HashMap<>();

    public UpdaterHolder(List<Updater> updaterList) {
        for (Updater updater : updaterList) {
            updaters.put(updater.getHost(), updater);
        }
    }

    public Optional<Updater> getUpdaterByHost(String host) {
        return Optional.ofNullable(updaters.get(host));

    }
}
