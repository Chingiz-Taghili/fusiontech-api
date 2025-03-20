package com.fusiontech.api.services;


import com.fusiontech.api.dtos.review.ReviewCreateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface ReviewService {

    ApiResponse getReviewsByProductId(Long productId, Integer pageNumber, Integer pageSize);

    ApiResponse createReview(ReviewCreateDto createDto, String userEmail);
}
