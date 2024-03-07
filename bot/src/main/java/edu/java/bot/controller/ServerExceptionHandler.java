package edu.java.bot.controller;

import edu.java.common.dto.responses.ApiErrorResponse;
import java.util.Collections;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class ServerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

        return new ApiErrorResponse(
            "Invalid request parameters",
            "4XX",
            "ValidationException",
            "One or more request parameters are invalid",
            errors
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiErrorResponse handleException(Exception exception) {
        return new ApiErrorResponse(
            "Server error",
            "5XX",
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            Collections.singletonList(exception.getMessage())
        );
    }
}
