package edu.java.scrapper.controller;

import edu.java.scrapper.exception.MissingChatException;
import edu.java.scrapper.exception.RepeatedLinkAdditionException;
import edu.java.scrapper.exception.RepeatedRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"edu.java.scrapper"})
public class ScrapperExceptionHandler {

    @ExceptionHandler(RepeatedRegistrationException.class)
    public ResponseEntity<String> handleRepeatedRegistrationException(RepeatedRegistrationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ошибка: Повторная регистрация.");
    }

    @ExceptionHandler(RepeatedLinkAdditionException.class)
    public ResponseEntity<String> handleRepeatedLinkAdditionException(RepeatedLinkAdditionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ошибка: Повторное добавление ссылки.");
    }

    @ExceptionHandler(MissingChatException.class)
    public ResponseEntity<String> handleMissingChatException(MissingChatException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка: Чат не найден.");
    }
}
