package com.fusiontech.api.services;

import com.fusiontech.api.dtos.brand.BrandCreateDto;
import com.fusiontech.api.dtos.brand.BrandUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface BrandService {

    ApiResponse getAllBrands();

    ApiResponse getBrandById(Long id);

    ApiResponse getSearchBrands(String keyword);

    ApiResponse getTotalCount();

    ApiResponse createBrand(BrandCreateDto createDto);

    ApiResponse updateBrand(Long id, BrandUpdateDto updateDto);

    ApiResponse deleteBrand(Long id);
}
