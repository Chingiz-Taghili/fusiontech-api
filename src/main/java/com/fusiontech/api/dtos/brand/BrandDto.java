package com.fusiontech.api.dtos.brand;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fusiontech.api.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    private Long id;
    private String name;
    @JsonIgnoreProperties("brand")
    private List<Product> products;
}
