package com.fusiontech.api.services;

import com.fusiontech.api.dtos.order.OrderCreateDto;
import com.fusiontech.api.dtos.order.OrderUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface OrderService {

    ApiResponse createOrder(String userEmail, OrderCreateDto createDto);

    ApiResponse updateOrder(Long id, OrderUpdateDto updateDto);

    ApiResponse deleteOrder(Long id);

    ApiResponse getOrderById(Long id);

    ApiResponse getOrderByOrderItemId(Long itemId);

    ApiResponse getAllOrders(Integer pageNumber, Integer pageSize);

    ApiResponse getSearchOrders(String keyword, Integer pageNumber, Integer pageSize);

    ApiResponse getTotalCount();
}
