package com.fusiontech.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    @ManyToOne
    private UserEntity user;
}
