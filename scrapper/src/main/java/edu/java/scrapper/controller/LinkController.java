package edu.java.scrapper.controller;


import edu.java.common.dto.requests.AddLinkRequest;
import edu.java.common.dto.requests.RemoveLinkRequest;
import edu.java.common.dto.responses.LinkResponse;
import edu.java.common.dto.responses.ListLinksResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/links")
@Slf4j
public class LinkController {

    @Operation(summary = "Получить все ссылки", description = "Получает список всех ссылок")
    @GetMapping
    public ListLinksResponse getAllLinks(
        @Parameter(description = "Идентификатор чата", required = true)
        @RequestHeader("Tg-Chat-Id") Long tgChatId) {

        log.info("Запрос на получение всех ссылок");
        List<LinkResponse> links = new ArrayList<>();
        int size = links.size();
        return new ListLinksResponse(links, size);
    }

    @Operation(summary = "Добавить ссылку", description = "Добавляет новую ссылку")
    @PostMapping
    public LinkResponse addLink(
        @Parameter(description = "Идентификатор чата", required = true)
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest request) {

        log.info("Запрос на добавление ссылки");
        return new LinkResponse(1L, request.link());
    }

    @Operation(summary = "Удалить ссылку", description = "Удаляет указанную ссылку")
    @DeleteMapping
    public LinkResponse removeLink(
        @Parameter(description = "Идентификатор чата", required = true)
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest request) {

        log.info("Запрос на удаление ссылки");
        return new LinkResponse(1L, request.link());
    }
}
