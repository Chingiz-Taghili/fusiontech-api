package com.fusiontech.api.services;

import com.fusiontech.api.dtos.cart.CartItemCreateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface CartItemService {

    ApiResponse addToCart(CartItemCreateDto createDto, String userEmail);

    ApiResponse deleteCartItem(Long productId, String userEmail);
}
