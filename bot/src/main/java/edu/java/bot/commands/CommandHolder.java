package edu.java.bot.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CommandHolder {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandHolder(List<Command> commandList) {
        for (Command command : commandList) {
            commands.put(command.getName(), command);
        }
    }

    public Optional<Command> getCommandByName(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    public Collection<Command> getAllCommands() {
        return commands.values();
    }
}
