package com.fusiontech.api.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse {
    private final boolean success;

    public ApiResponse(boolean success) {
        this.success = success;
    }
}
