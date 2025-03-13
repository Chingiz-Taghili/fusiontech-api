package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.product.ProductCreateDto;
import com.fusiontech.api.dtos.product.ProductUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = productService.getAllProducts(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ApiResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse> getFilteredProducts(
            @RequestParam Integer price, @RequestParam Long categoryId, @RequestParam Long brandId,
            @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = productService.getFilteredProducts(price, categoryId, brandId, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/catalog")
    public ResponseEntity<ApiResponse> getCatalogProducts(
            @RequestParam Long categoryId, @RequestParam Long subcategoryId,
            @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = productService.getCatalogProducts(categoryId, subcategoryId, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearchProducts(@RequestParam String keyword,
            @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = productService.getSearchProducts(keyword, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse> getFeaturedProducts() {
        ApiResponse response = productService.getFeaturedProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<ApiResponse> getRelatedProducts(@PathVariable Long productId, @RequestParam Long categoryId) {
        ApiResponse response = productService.getRelatedProducts(categoryId, productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/offers")
    public ResponseEntity<ApiResponse> getOfferedProducts() {
        ApiResponse response = productService.getOfferedProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getTotalCount() {
        ApiResponse response = productService.getTotalCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-ranges")
    public ResponseEntity<ApiResponse> getCountByPriceRanges() {
        ApiResponse response = productService.getCountByPriceRanges();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductCreateDto createDto) {
        ApiResponse response = productService.createProduct(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDto updateDto) {
        ApiResponse response = productService.updateProduct(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/rating")
    public ResponseEntity<ApiResponse> updateProductRating(@PathVariable Long id) {
        ApiResponse response = productService.updateProductRating(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        ApiResponse response = productService.deleteProduct(id);
        return ResponseEntity.ok(response);
    }
}
