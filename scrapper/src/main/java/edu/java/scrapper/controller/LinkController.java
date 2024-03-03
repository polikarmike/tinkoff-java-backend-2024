package edu.java.scrapper.controller;


import edu.java.common.dto.requests.AddLinkRequest;
import edu.java.common.dto.requests.RemoveLinkRequest;
import edu.java.common.dto.responses.LinkResponse;
import edu.java.common.dto.responses.ListLinksResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {

        log.info("Запрос на получение всех ссылок");
        List<LinkResponse> links = new ArrayList<>();
        int size = links.size();
        ListLinksResponse response = new ListLinksResponse(links, size);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody
    AddLinkRequest request) {

        log.info("Запрос на добавление ссылки");
        LinkResponse createdLink = new LinkResponse(1L, request.link());

        return ResponseEntity.ok(createdLink);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody
    RemoveLinkRequest request) {

        log.info("Запрос на удаление всех ссылки");
        LinkResponse removedLink = new LinkResponse(1L, request.link());

        return ResponseEntity.ok(removedLink);
    }
}
