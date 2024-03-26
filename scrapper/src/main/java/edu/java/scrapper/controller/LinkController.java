package edu.java.scrapper.controller;

import edu.java.common.dto.requests.AddLinkRequest;
import edu.java.common.dto.requests.RemoveLinkRequest;
import edu.java.common.dto.responses.LinkResponse;
import edu.java.common.dto.responses.ListLinksResponse;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.service.jdbc.JDBCLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class LinkController {
    private final JDBCLinkService linkService;

    @Operation(summary = "Получить все ссылки", description = "Получает список всех ссылок")
    @GetMapping
    public ListLinksResponse getAllLinks(
        @Parameter(description = "Идентификатор чата", required = true)
        @Valid @RequestHeader("Tg-Chat-Id") @NotNull Long tgChatId) {

        log.info("Запрос на получение всех ссылок");

        Collection<Link> links = linkService.listAll(tgChatId);
        List<LinkResponse> linkResponses = links.stream()
            .map(link -> new LinkResponse(link.getId(), link.getUri()))
            .toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Operation(summary = "Добавить ссылку", description = "Добавляет новую ссылку")
    @PostMapping
    public LinkResponse addLink(
        @Parameter(description = "Идентификатор чата", required = true)
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @Valid @RequestBody AddLinkRequest request) {

        log.info("Запрос на добавление ссылки");

        Link link = linkService.add(tgChatId, request.link());
        return new LinkResponse(tgChatId, link.getUri());
    }

    @Operation(summary = "Удалить ссылку", description = "Удаляет указанную ссылку")
    @DeleteMapping
    public LinkResponse removeLink(
        @Parameter(description = "Идентификатор чата", required = true)
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @Valid @RequestBody RemoveLinkRequest request) {

        log.info("Запрос на удаление ссылки");

        Link link = linkService.remove(tgChatId, request.link());
        return new LinkResponse(tgChatId, link.getUri());
    }
}
