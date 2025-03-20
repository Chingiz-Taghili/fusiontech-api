package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.review.ReviewCreateDto;
import com.fusiontech.api.dtos.review.ReviewDto;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Product;
import com.fusiontech.api.models.Review;
import com.fusiontech.api.models.UserEntity;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.payloads.Paged;
import com.fusiontech.api.repositories.ProductRepository;
import com.fusiontech.api.repositories.ReviewRepository;
import com.fusiontech.api.repositories.UserRepository;
import com.fusiontech.api.services.ProductService;
import com.fusiontech.api.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ProductService productService, ProductRepository productRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getReviewsByProductId(Long productId, Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "writingDate"));
        Page<Review> findReviews = reviewRepository.findByProductId(productId, pageable);
        if (findReviews.isEmpty()) {
            return new MessageResponse("No reviews available for the product id: " + productId);
        }
        Page<ReviewDto> reviews = findReviews.map(review -> modelMapper.map(review, ReviewDto.class));
        return new DataResponse<>(new Paged<>(reviews.getContent(), pageNumber, reviews.getTotalPages()));
    }

    @Transactional
    @Override
    public ApiResponse createReview(ReviewCreateDto createDto, String userEmail) {
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));
        Product findProduct = productRepository.findById(createDto.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", createDto.getProductId()));

        Review review = new Review();
        review.setUserName(findUser.getName());
        review.setUserSurname(findUser.getSurname());
        review.setUserImage(Optional.ofNullable(findUser.getImageUrl()).orElse("/default-profile-picture.svg"));
        review.setComment(createDto.getComment());
        review.setRating(createDto.getRating());
        review.setWritingDate(LocalDateTime.now());
        review.setProduct(findProduct);
        reviewRepository.save(review);

        productService.updateProductRating(findProduct.getId());
        return new MessageResponse("Review created successfully");
    }
}
