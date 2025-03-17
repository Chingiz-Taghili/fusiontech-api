package com.fusiontech.api.services;

import com.fusiontech.api.dtos.orderItem.OrderItemCreateDto;
import com.fusiontech.api.dtos.orderItem.OrderItemUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface OrderItemService {

    ApiResponse getOrderItemById(Long id);

    ApiResponse createOrderItem(Long orderId, OrderItemCreateDto createDto);

    ApiResponse updateOrderItem(Long id, OrderItemUpdateDto updateDto);

    ApiResponse deleteOrderItem(Long id);
}