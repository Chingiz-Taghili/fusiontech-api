package com.fusiontech.api.security.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthErrorResponse {
    private int status;
    private String message;
}
