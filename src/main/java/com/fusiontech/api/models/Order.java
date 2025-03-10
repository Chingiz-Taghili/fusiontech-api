package com.fusiontech.api.models;

import com.fusiontech.api.enums.OrderStatus;
import com.fusiontech.api.enums.PaymentMethod;
import com.fusiontech.api.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity user;
    private LocalDateTime orderDate;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String city;
    private String address;
    private String message;

    private Boolean differBilling;
    private String billName;
    private String billSurname;
    private String billPhoneNumber;
    private String billEmail;
    private String billCity;
    private String billAddress;
    private String billMessage;

    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
