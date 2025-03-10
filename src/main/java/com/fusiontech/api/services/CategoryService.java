package com.fusiontech.api.services;

import com.fusiontech.api.dtos.category.CategoryCreateDto;
import com.fusiontech.api.dtos.category.CategoryUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface CategoryService {

    ApiResponse getAllCategories();

    ApiResponse getSearchCategories(String keyword);

    ApiResponse getCategoryById(Long id);

    ApiResponse createCategory(CategoryCreateDto categoryCreateDto);

    ApiResponse updateCategory(Long id, CategoryUpdateDto categoryUpdateDto);

    ApiResponse deleteCategory(Long id);

    ApiResponse getTotalCount();
}
