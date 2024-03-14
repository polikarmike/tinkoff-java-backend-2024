package edu.java.bot.exception;

import edu.java.common.dto.responses.ApiErrorResponse;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final ApiErrorResponse apiErrorResponse;

    public BadRequestException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.description());
        this.apiErrorResponse = apiErrorResponse;
    }
}
