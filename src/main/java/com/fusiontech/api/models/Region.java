package com.fusiontech.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer number;
}
