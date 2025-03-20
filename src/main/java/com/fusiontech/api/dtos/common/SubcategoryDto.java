package com.fusiontech.api.dtos.common;

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
    private Category category;
    private SubcategoryName subcategoryName;
}
