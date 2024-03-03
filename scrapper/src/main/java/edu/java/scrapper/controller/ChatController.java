package edu.java.scrapper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@Slf4j
public class ChatController {

    @PostMapping("/{id}")
    public ResponseEntity<String> registerChat(@PathVariable Long id) {
        log.info("Регистрация чата");
        return ResponseEntity.ok("Чат зарегистрирован");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable Long id) {
        log.info("Удаление чата");
        return ResponseEntity.ok("Чат успешно удалён");
    }
}
