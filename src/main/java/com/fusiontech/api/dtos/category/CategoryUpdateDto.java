package com.fusiontech.api.dtos.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDto {
    private String name;
    private String imageUrl;
    private List<String> subcategoriesName;
}
