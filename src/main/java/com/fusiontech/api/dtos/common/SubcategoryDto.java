package com.fusiontech.api.dtos.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fusiontech.api.models.Category;
import com.fusiontech.api.models.SubcategoryName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryDto {
    private Long id;
    @JsonIgnoreProperties({"subcategories", "products"})
    private Category category;
    private SubcategoryName subcategoryName;
}
