package com.scentalux.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(nullable = false)
    private Double subtotal;

    @Column(nullable = false)
    private Double taxes;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false, length = 50)
    private String status = "PENDIENTE"; // PENDIENTE, CONFIRMADO, ENVIADO, ENTREGADO, CANCELADO

    @Column(nullable = false, length = 20)
    private String paymentMethod; // YAPE, PLIN

    @Column(length = 255)
    private String receiptImageUrl;

    // Información de envío
    private String customerName;
    private String shippingAddress;
    private String city;
    private String postalCode;
    private String phone;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void generateOrderNumber() {
        this.orderNumber = "ORD" + System.currentTimeMillis();
    }
}