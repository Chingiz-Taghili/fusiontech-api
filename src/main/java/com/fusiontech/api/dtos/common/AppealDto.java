package com.fusiontech.api.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppealDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String subject;
    private String message;
}
