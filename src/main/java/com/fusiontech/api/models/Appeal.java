package com.fusiontech.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "appeals")
public class Appeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String subject;
    private String message;
}
