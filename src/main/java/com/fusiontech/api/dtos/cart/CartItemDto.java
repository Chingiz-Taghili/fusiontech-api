package com.fusiontech.api.dtos.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private String name;
    private double price;
    private double discountPrice;
    private String image;
    private int quantity;
    private double amount;
}
