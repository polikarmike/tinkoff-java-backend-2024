package edu.java.bot.commands;

import edu.java.bot.commands.impl.HelpCommand;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CommandHolder {
    private final Map<String, ICommand> commands = new LinkedHashMap<>();
    private final HelpCommand helpCommand;

    public CommandHolder(List<ICommand> commandList, HelpCommand helpCommand) {
        this.helpCommand = helpCommand;
        for (ICommand command : commandList) {
            commands.put(command.getName(), command);
        }
    }

    public ICommand getCommandByName(String name) {
        return commands.getOrDefault(name, helpCommand);
    }

    public Map<String, ICommand> getAllCommands() {
        return commands;
    }
}
