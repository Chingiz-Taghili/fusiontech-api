package com.fusiontech.api.dtos.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fusiontech.api.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private double price;
    private String description;
    private String moreDetail;
    private double discountPrice;
    private double discountPercent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime discountDate;
    private boolean featured;
    private boolean offered;
    private double rating;
    private List<Image> images;
    private String imageUrl;
    private List<Review> reviews;
    private Brand brand;
    private Category category;
    private Subcategory subcategory;
}
