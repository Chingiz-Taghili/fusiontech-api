package com.fusiontech.api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String userSurname;
    private String userImage;
    @Column(length = 600)
    private String comment;
    private Integer rating;
    private LocalDateTime writingDate;
    @ManyToOne
    private Product product;
}
