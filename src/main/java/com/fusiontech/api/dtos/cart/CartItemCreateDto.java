package com.fusiontech.api.dtos.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemCreateDto {
    private Long productId;
    private int quantity;
}
