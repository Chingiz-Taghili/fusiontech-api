package com.fusiontech.api.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {
    private String name;
    private double price;
    private String description;
    private String moreDetail;
    private double discountPrice;
    private LocalDateTime discountDate;
    private boolean featured;
    private boolean offered;
    private List<String> imageUrls;
    private Long brand;
    private Long category;
    private Long subcategory;
}
