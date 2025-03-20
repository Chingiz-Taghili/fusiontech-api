package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.order.OrderCreateDto;
import com.fusiontech.api.dtos.order.OrderDto;
import com.fusiontech.api.dtos.order.OrderUpdateDto;
import com.fusiontech.api.enums.OrderStatus;
import com.fusiontech.api.enums.PaymentStatus;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.CartItem;
import com.fusiontech.api.models.Order;
import com.fusiontech.api.models.OrderItem;
import com.fusiontech.api.models.UserEntity;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.payloads.Paged;
import com.fusiontech.api.repositories.CartItemRepository;
import com.fusiontech.api.repositories.OrderItemRepository;
import com.fusiontech.api.repositories.OrderRepository;
import com.fusiontech.api.repositories.UserRepository;
import com.fusiontech.api.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, CartItemRepository cartItemRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public ApiResponse createOrder(String userEmail, OrderCreateDto createDto) {
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));
        List<CartItem> findItems = findUser.getCartItems();
        if (findItems.isEmpty()) {
            throw new IllegalStateException("No Cart items found for the current user");
        }
        Order order = modelMapper.map(createDto, Order.class);
        order.setUser(findUser);
        order.setOrderDate(LocalDateTime.now());

        if (!createDto.isDifferBilling()) {
            order.setBillName(order.getName());
            order.setBillSurname(order.getSurname());
            order.setBillPhoneNumber(order.getPhoneNumber());
            order.setBillEmail(order.getEmail());
            order.setBillCity(order.getCity());
            order.setBillAddress(order.getAddress());
            order.setBillMessage(order.getMessage());
        }
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        orderRepository.save(order);

        List<OrderItem> orderItems = findItems.stream().map(findItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(findItem.getProduct());
            orderItem.setPrice(findItem.getProduct().getDiscountPrice());
            orderItem.setQuantity(findItem.getQuantity());
            orderItem.setOrder(order);
            return orderItem;
        }).toList();
        orderItemRepository.saveAll(orderItems);

        cartItemRepository.deleteAll(findItems);
        return new MessageResponse("Order created successfully");
    }

    @Transactional
    @Override
    public ApiResponse updateOrder(Long id, OrderUpdateDto updateDto) {
        Order findOrder = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id));

        findOrder.setName(updateDto.getName());
        findOrder.setSurname(updateDto.getSurname());
        findOrder.setPhoneNumber(updateDto.getPhoneNumber());
        findOrder.setEmail(updateDto.getEmail());
        findOrder.setCity(updateDto.getCity());
        findOrder.setAddress(updateDto.getAddress());
        findOrder.setMessage(updateDto.getMessage());
        findOrder.setDifferBilling(updateDto.isDifferBilling());

        if (!updateDto.isDifferBilling()) {
            findOrder.setBillName(updateDto.getName());
            findOrder.setBillSurname(updateDto.getSurname());
            findOrder.setBillPhoneNumber(updateDto.getPhoneNumber());
            findOrder.setBillEmail(updateDto.getEmail());
            findOrder.setBillCity(updateDto.getCity());
            findOrder.setBillAddress(updateDto.getAddress());
            findOrder.setBillMessage(updateDto.getMessage());
        } else {
            findOrder.setBillName(updateDto.getBillName());
            findOrder.setBillSurname(updateDto.getBillSurname());
            findOrder.setBillPhoneNumber(updateDto.getBillPhoneNumber());
            findOrder.setBillEmail(updateDto.getBillEmail());
            findOrder.setBillCity(updateDto.getBillCity());
            findOrder.setBillAddress(updateDto.getBillAddress());
            findOrder.setBillMessage(updateDto.getBillMessage());
        }
        findOrder.setPaymentMethod(updateDto.getPaymentMethod());
        findOrder.setOrderStatus(updateDto.getOrderStatus());
        findOrder.setPaymentStatus(updateDto.getPaymentStatus());
        orderRepository.save(findOrder);
        return new MessageResponse("Order updated successfully");
    }

    @Transactional
    @Override
    public ApiResponse deleteOrder(Long id) {
        Order findOrder = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id));
        orderRepository.delete(findOrder);
        return new MessageResponse("Order deleted successfully");
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getOrderById(Long id) {
        Order findOrder = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id));
        return new DataResponse<>(modelMapper.map(findOrder, OrderDto.class));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getOrderByOrderItemId(Long itemId) {
        Order findOrder = orderRepository.findOrderByOrderItemId(itemId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "Order item id", itemId));
        return new DataResponse<>(modelMapper.map(findOrder, OrderDto.class));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllOrders(Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Order> findOrders = orderRepository.findAll(pageable);
        if (findOrders.isEmpty()) {
            return new MessageResponse("No orders available");
        }
        Page<OrderDto> orders = findOrders.map(order -> modelMapper.map(order, OrderDto.class));
        return new DataResponse<>(new Paged<>(orders.getContent(), pageNumber, orders.getTotalPages()));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSearchOrders(String keyword, Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Order> findOrders = orderRepository.findByKeywordInColumnsIgnoreCase(keyword, pageable);
        if (findOrders.isEmpty()) {
            return new MessageResponse("No orders found for the keyword: " + keyword);
        }
        Page<OrderDto> orders = findOrders.map(order -> modelMapper.map(order, OrderDto.class));
        return new DataResponse<>(new Paged<>(orders.getContent(), pageNumber, orders.getTotalPages()));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getTotalCount() {
        return new DataResponse<>(orderRepository.count());
    }
}
