package com.fusiontech.api.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialDto {
    private Long id;
    private String name;
    private String surname;
    private String position;
    private String image;
    private String description;
}
