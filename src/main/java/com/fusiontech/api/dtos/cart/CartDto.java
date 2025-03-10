package com.fusiontech.api.dtos.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private List<CartItemDto> cartItems;
    private double subtotal;
    private double shipping;
    private double total;
}
