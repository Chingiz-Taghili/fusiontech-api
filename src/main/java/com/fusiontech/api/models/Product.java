package com.fusiontech.api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private String description;
    @Column(length = 1500)
    private String moreDetail;
    private Double discountPrice;
    private LocalDateTime discountDate;
    private Boolean featured;
    private Boolean offered;
    private Double rating;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
    /* cascade = CascadeType.ALL: `Product` saxlanarkən, əlaqəli `Image` obyektləri də avtomatik
       saxlanır; yeniləndikdə/silinəndə də bu əməliyyatlar onlara tətbiq olunur.
       orphanRemoval = true: `Product`-dan bir `Image` silindikdə, həmin şəkil database-dən də
       avtomatik silinir. */

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    @ManyToOne
    private Brand brand;
    @ManyToOne
    private Category category;
    @ManyToOne
    private Subcategory subcategory;
}
