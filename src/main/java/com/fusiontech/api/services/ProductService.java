package com.fusiontech.api.services;

import com.fusiontech.api.dtos.product.ProductCreateDto;
import com.fusiontech.api.dtos.product.ProductUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface ProductService {

    ApiResponse createProduct(ProductCreateDto createDto);

    ApiResponse updateProduct(Long id, ProductUpdateDto updateDto);

    ApiResponse deleteProduct(Long id);

    ApiResponse getProductById(Long id);

    ApiResponse getAllProducts(Integer pageNumber, Integer pageSize);

    ApiResponse getFilteredProducts(int price, int category, int brand, Integer pageNumber, Integer pageSize);

    ApiResponse getCatalogProducts(Long categoryId, Long subcategoryId, Integer pageNumber, Integer pageSize);

    ApiResponse getSearchProducts(String keyword, Integer pageNumber, Integer pageSize);

    ApiResponse getFeaturedProducts();

    ApiResponse getRelatedProducts(Long categoryId, Long productId);

    ApiResponse getOfferedProducts();

    ApiResponse getTotalProductCount();

    ApiResponse getCountByPriceRanges();

    ApiResponse updateProductRating(Long productId);
}