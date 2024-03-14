package edu.java.bot.controller;

import edu.java.bot.service.LinkUpdateService;
import edu.java.common.dto.requests.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class BotController {
    private final LinkUpdateService linkUpdateService;

    @Operation(summary = "Обработка обновления ссылки", description = "Обрабатывает поступившее обновление ссылки")
    @PostMapping("/updates")
    public String handleUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {
        linkUpdateService.processLinkUpdate(linkUpdateRequest);
        return "Обновление обработано";
    }
}
