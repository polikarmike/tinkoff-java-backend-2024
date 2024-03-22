package edu.java.scrapper.updater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

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
