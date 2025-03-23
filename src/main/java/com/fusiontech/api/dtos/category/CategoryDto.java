package com.fusiontech.api.dtos.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fusiontech.api.models.Product;
import com.fusiontech.api.models.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Integer productQuantity;
    //Yalnız bu dto-da subcategory-nin "category"-sini gizlədir. SubcategoryDto-a görünməyə davam edir
    @JsonIgnoreProperties("category")
    private List<Subcategory> subcategories;
    @JsonIgnoreProperties({"category", "subcategory"})
    private List<Product> products;
}
