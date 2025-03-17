package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.orderItem.OrderItemCreateDto;
import com.fusiontech.api.dtos.orderItem.OrderItemDto;
import com.fusiontech.api.dtos.orderItem.OrderItemUpdateDto;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Order;
import com.fusiontech.api.models.OrderItem;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.OrderItemRepository;
import com.fusiontech.api.repositories.OrderRepository;
import com.fusiontech.api.services.OrderItemService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getOrderItemById(Long id) {
        OrderItem findItem = orderItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order item", "id", id));
        OrderItemDto orderItem = modelMapper.map(findItem, OrderItemDto.class);
        return new DataResponse<>(orderItem);
    }

    @Transactional
    @Override
    public ApiResponse createOrderItem(Long orderId, OrderItemCreateDto createDto) {
        Order findOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", orderId));
        OrderItem newItem = modelMapper.map(createDto, OrderItem.class);
        newItem.setOrder(findOrder);
        orderItemRepository.save(newItem);
        return new MessageResponse("Order item created successfully");
    }

    @Transactional
    @Override
    public ApiResponse updateOrderItem(Long id, OrderItemUpdateDto updateDto) {
        OrderItem findItem = orderItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order item", "id", id));
        findItem.setPrice(updateDto.getPrice());
        findItem.setQuantity(updateDto.getQuantity());
        orderItemRepository.save(findItem);
        return new MessageResponse("Order item updated successfully");
    }

    @Transactional
    @Override
    public ApiResponse deleteOrderItem(Long id) {
        OrderItem findItem = orderItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order item", "id", id));
        orderItemRepository.delete(findItem);
        return new MessageResponse("Order item deleted successfully");
    }
}
