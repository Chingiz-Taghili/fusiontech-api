package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.brand.BrandCreateDto;
import com.fusiontech.api.dtos.brand.BrandUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBrandById(@PathVariable Long id) {
        ApiResponse response = brandService.getBrandById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearchBrands(@RequestParam String keyword) {
        ApiResponse response = brandService.getSearchBrands(keyword);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createBrand(@RequestBody BrandCreateDto createDto) {
        ApiResponse response = brandService.createBrand(createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBrand(@PathVariable Long id, @RequestBody BrandUpdateDto updateDto) {
        ApiResponse response = brandService.updateBrand(id, updateDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBrand(@PathVariable Long id) {
        ApiResponse response = brandService.deleteBrand(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getTotalCount() {
        ApiResponse response = brandService.getTotalCount();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
