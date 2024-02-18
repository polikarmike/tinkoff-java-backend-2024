package edu.java.bot.service.commands.impl;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.commands.DefaultCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HelpCommand extends DefaultCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);
    private static final String HELP_COMMAND = "/help";
    private static final String HELP_RESPONSE = "Вот список доступных команд:\n\n"
        + "/start - Зарегистрировать пользователя\n"
        + "/help - Вывести это окно с командами\n"
        + "/track - Начать отслеживание ссылки\n"
        + "/untrack - Прекратить отслеживание ссылки\n"
        + "/list - Показать список отслеживаемых ссылок";

    @Override
    public String execute(Update update) {
        if (update.message().text().equals(HELP_COMMAND)) {
            LOGGER.info("Help command executed");
            return HELP_RESPONSE;
        }

        return super.execute(update);
    }
}
