package com.scentalux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Integer perfumeId;
    private String perfumeName;
    private String brand;
    private String imageUrl;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}