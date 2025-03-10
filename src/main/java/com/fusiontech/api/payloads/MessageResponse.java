package com.fusiontech.api.payloads;

import lombok.Getter;

@Getter
public class MessageResponse extends ApiResponse {

    private final String message;

    public MessageResponse(String message) {
        super(true);
        this.message = message;
    }
}
