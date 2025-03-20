package com.fusiontech.api.services;

import com.fusiontech.api.payloads.ApiResponse;

public interface SubcategoryService {

    ApiResponse getAllSubcategories();

    ApiResponse getSubcategoryById(Long id);
}
