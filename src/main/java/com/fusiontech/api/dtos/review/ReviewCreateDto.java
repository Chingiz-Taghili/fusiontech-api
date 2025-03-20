package com.fusiontech.api.dtos.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDto {
    private String comment;
    private int rating;
    private Long productId;
}
