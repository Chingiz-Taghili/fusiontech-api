package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.orderItem.OrderItemCreateDto;
import com.fusiontech.api.dtos.orderItem.OrderItemUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.OrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private final OrderItemService orderItemService;

    public OrderController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderItemById(@PathVariable Long id) {
        ApiResponse response = orderItemService.getOrderItemById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResponse> createOrderItem(@PathVariable Long orderId, @RequestBody OrderItemCreateDto createDto) {
        ApiResponse response = orderItemService.createOrderItem(orderId, createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOrderItem(@PathVariable Long id, @RequestBody OrderItemUpdateDto updateDto) {
        ApiResponse response = orderItemService.updateOrderItem(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOrderItem(@PathVariable Long id) {
        ApiResponse response = orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok(response);
    }
}
