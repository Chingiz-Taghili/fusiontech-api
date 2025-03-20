package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.review.ReviewCreateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.RegionService;
import com.fusiontech.api.services.ReviewService;
import com.fusiontech.api.services.SubcategoryService;
import com.fusiontech.api.services.TestimonialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api")
public class CommonController {

    private final RegionService regionService;
    private final ReviewService reviewService;
    private final SubcategoryService subcategoryService;
    private final TestimonialService testimonialService;

    public CommonController(RegionService regionService, ReviewService reviewService, SubcategoryService subcategoryService, TestimonialService testimonialService) {
        this.regionService = regionService;
        this.reviewService = reviewService;
        this.subcategoryService = subcategoryService;
        this.testimonialService = testimonialService;
    }

    @GetMapping("/regions")
    public ResponseEntity<ApiResponse> getRegions() {
        ApiResponse response = regionService.getRegions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reviews/{productId}")
    public ResponseEntity<ApiResponse> getReviewsByProductId(@PathVariable Long productId,
                                                             @RequestParam(required = false) Integer pageNumber,
                                                             @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = reviewService.getReviewsByProductId(productId, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse> createReview(@RequestBody ReviewCreateDto createDto, Principal principal) {
        ApiResponse response = reviewService.createReview(createDto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/subcategories")
    public ResponseEntity<ApiResponse> getAllSubcategories() {
        ApiResponse response = subcategoryService.getAllSubcategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subcategories/{id}")
    public ResponseEntity<ApiResponse> getSubcategoryById(@PathVariable Long id) {
        ApiResponse response = subcategoryService.getSubcategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/testimonials")
    public ResponseEntity<ApiResponse> getAllTestimonials() {
        ApiResponse response = testimonialService.getAllTestimonials();
        return ResponseEntity.ok(response);
    }
}
