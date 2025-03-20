package com.fusiontech.api.dtos.slider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SliderDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String url;
    private boolean active;
}
