package com.fusiontech.api.dtos.orderItem;

import com.fusiontech.api.models.Order;
import com.fusiontech.api.models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private Product product;
    private double price;
    private int quantity;
    private Order order;
}
