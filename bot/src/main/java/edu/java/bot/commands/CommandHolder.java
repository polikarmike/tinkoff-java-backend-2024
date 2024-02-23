package edu.java.bot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CommandHolder {
    private final List<Command> commands = new ArrayList<>();

    public CommandHolder(List<Command> commandList) {
        commands.addAll(commandList);
    }

    public Optional<Command> getCommandByName(String name) {
        return commands.stream()
            .filter(command -> command.getName().equals(name))
            .findFirst();
    }

    public List<Command> getAllCommands() {
        return commands;
    }
}
