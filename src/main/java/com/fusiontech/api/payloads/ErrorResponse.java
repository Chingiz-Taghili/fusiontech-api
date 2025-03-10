package com.fusiontech.api.payloads;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse extends ApiResponse {

    private final String message;
    private final int status;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message, int status) {
        super(false);
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message) {
        super(false);
        this.message = message;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.timestamp = LocalDateTime.now();
    }
}
