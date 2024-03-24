package edu.java.scrapper.controller;

import edu.java.scrapper.service.TgChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final TgChatService tgChatService;

    @Operation(summary = "Регистрация чата", description = "Регистрирует чат с указанным идентификатором")
    @PostMapping("/{id}")
    public String registerChat(
        @Parameter(description = "Идентификатор чата", required = true) @Valid @PathVariable @NotNull Long id
    ) {
        tgChatService.register(id);
        log.info("Регистрация чата");
        return "Чат зарегистрирован";
    }

    @Operation(summary = "Удаление чата", description = "Удаляет чат с указанным идентификатором")
    @DeleteMapping("/{id}")
    public String deleteChat(
        @Parameter(description = "Идентификатор чата", required = true) @Valid @PathVariable @NotNull Long id
    ) {
        tgChatService.unregister(id);
        log.info("Удаление чата");
        return "Чат успешно удалён";
    }
}
