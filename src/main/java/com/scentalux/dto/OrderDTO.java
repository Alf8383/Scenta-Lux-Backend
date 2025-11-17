package com.scentalux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer id;
    private String orderNumber;
    private String customerName;
    private LocalDateTime orderDate;
    private Double subtotal;
    private Double taxes;
    private Double total;
    private String status;
    private String paymentMethod;
    private String receiptImageUrl;
    private String shippingAddress;
    private String city;
    private String postalCode;
    private List<OrderItemResponseDTO> items;
}