package com.fusiontech.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sliders")
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String url;
    private Boolean active;
}
