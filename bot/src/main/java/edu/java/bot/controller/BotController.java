package edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.Bot;
import edu.java.common.dto.requests.LinkUpdateRequest;
import edu.java.common.dto.responses.ApiErrorResponse;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class BotController {
    private final Bot bot;
    private static final String URL_REQUIRED = "URL is required";

    @PostMapping("/updates")
    public ResponseEntity<String> handleUpdate(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        if (linkUpdateRequest.url() == null) {
            ApiErrorResponse errorResponse = new ApiErrorResponse("Некорректные параметры запроса",
                "400",
                "InvalidUrlException",
                URL_REQUIRED,
                Arrays.asList(URL_REQUIRED)
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.toString());
        }

        for (Long chatId : linkUpdateRequest.tgChatIds()) {
            bot.execute(new SendMessage(chatId, "Уведомление из Scrapper"));
        }

        return ResponseEntity.ok("Обновление обработано");
    }
}
