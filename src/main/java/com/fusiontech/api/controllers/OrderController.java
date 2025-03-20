package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.order.OrderCreateDto;
import com.fusiontech.api.dtos.order.OrderUpdateDto;
import com.fusiontech.api.dtos.orderItem.OrderItemCreateDto;
import com.fusiontech.api.dtos.orderItem.OrderItemUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.OrderItemService;
import com.fusiontech.api.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrders(@RequestParam(required = false) Integer pageNumber,
                                                    @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = orderService.getAllOrders(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearchOrders(@RequestParam String keyword,
                                                       @RequestParam(required = false) Integer pageNumber,
                                                       @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = orderService.getSearchOrders(keyword, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        ApiResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-item/{itemId}")
    public ResponseEntity<ApiResponse> getOrderByOrderItemId(@PathVariable Long itemId) {
        ApiResponse response = orderService.getOrderByOrderItemId(itemId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getTotalCount() {
        ApiResponse response = orderService.getTotalCount();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(Principal principal, @RequestBody OrderCreateDto createDto) {
        ApiResponse response = orderService.createOrder(principal.getName(), createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable Long id, @RequestBody OrderUpdateDto updateDto) {
        ApiResponse response = orderService.updateOrder(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long id) {
        ApiResponse response = orderService.deleteOrder(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ApiResponse> getOrderItemById(@PathVariable Long id) {
        ApiResponse response = orderItemService.getOrderItemById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items/{orderId}")
    public ResponseEntity<ApiResponse> createOrderItem(@PathVariable Long orderId, @RequestBody OrderItemCreateDto createDto) {
        ApiResponse response = orderItemService.createOrderItem(orderId, createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ApiResponse> updateOrderItem(@PathVariable Long id, @RequestBody OrderItemUpdateDto updateDto) {
        ApiResponse response = orderItemService.updateOrderItem(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponse> deleteOrderItem(@PathVariable Long id) {
        ApiResponse response = orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok(response);
    }
}
