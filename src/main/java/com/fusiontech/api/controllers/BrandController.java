package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.brand.BrandCreateDto;
import com.fusiontech.api.dtos.brand.BrandUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllBrands() {
        ApiResponse response = brandService.getAllBrands();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBrandById(@PathVariable Long id) {
        ApiResponse response = brandService.getBrandById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearchBrands(@RequestParam String keyword) {
        ApiResponse response = brandService.getSearchBrands(keyword);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createBrand(@RequestBody BrandCreateDto createDto) {
        ApiResponse response = brandService.createBrand(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateBrand(@PathVariable Long id, @RequestBody BrandUpdateDto updateDto) {
        ApiResponse response = brandService.updateBrand(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteBrand(@PathVariable Long id) {
        ApiResponse response = brandService.deleteBrand(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getTotalCount() {
        ApiResponse response = brandService.getTotalCount();
        return ResponseEntity.ok(response);
    }
}
