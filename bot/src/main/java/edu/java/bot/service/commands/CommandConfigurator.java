package edu.java.bot.service.commands;

import edu.java.bot.service.commands.impl.HelpCommand;
import edu.java.bot.service.commands.impl.ListCommand;
import edu.java.bot.service.commands.impl.StartCommand;
import edu.java.bot.service.commands.impl.TrackCommand;
import edu.java.bot.service.commands.impl.UntrackCommand;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandConfigurator {
    public ICommand configureCommands() {
        List<ICommand> commands = Arrays.asList(
            new StartCommand(),
            new HelpCommand(),
            new TrackCommand(),
            new UntrackCommand(),
            new ListCommand()
        );

        for (int i = 0; i < commands.size() - 1; i++) {
            commands.get(i).setNextCommand(commands.get(i + 1));
        }

        return commands.get(0);
    }
}
