package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.cart.CartItemCreateDto;
import com.fusiontech.api.dtos.review.ReviewCreateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api")
public class CommonController {

    private final CartItemService cartItemService;
    private final FavoritesService favoritesService;
    private final RegionService regionService;
    private final ReviewService reviewService;
    private final SubcategoryService subcategoryService;
    private final TestimonialService testimonialService;

    public CommonController(CartItemService cartItemService, FavoritesService favoritesService, RegionService regionService, ReviewService reviewService, SubcategoryService subcategoryService, TestimonialService testimonialService) {
        this.cartItemService = cartItemService;
        this.favoritesService = favoritesService;
        this.regionService = regionService;
        this.reviewService = reviewService;
        this.subcategoryService = subcategoryService;
        this.testimonialService = testimonialService;
    }

    @PostMapping("/cart")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody CartItemCreateDto createDto, Principal principal) {
        ApiResponse response = cartItemService.addToCart(createDto, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long id, Principal principal) {
        ApiResponse response = cartItemService.deleteCartItem(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/favorite/{productId}")
    public ResponseEntity<ApiResponse> addToFavorites(@PathVariable Long productId, Principal principal) {
        ApiResponse response = favoritesService.addToFavorites(productId, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/favorite/{id}")
    public ResponseEntity<ApiResponse> deleteFavorite(@PathVariable Long id, Principal principal) {
        ApiResponse response = favoritesService.deleteFavorite(id, principal.getName());
        return ResponseEntity.ok(response);
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
