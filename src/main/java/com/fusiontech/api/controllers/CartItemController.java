package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.cart.CartItemCreateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/cart")
public class CartItemController {
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@RequestBody CartItemCreateDto createDto, Principal principal) {
        ApiResponse response = cartItemService.addToCart(createDto, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long id, Principal principal) {
        ApiResponse response = cartItemService.deleteCartItem(id, principal.getName());
        return ResponseEntity.ok(response);
    }
}
