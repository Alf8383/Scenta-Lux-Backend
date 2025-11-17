package com.scentalux.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {
    @NotEmpty(message = "Los items del pedido no pueden estar vacíos")
    private List<OrderItemDTO> items;

    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String customerName;

    @NotBlank(message = "La dirección es obligatoria")
    private String shippingAddress;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "El código postal es obligatorio")
    private String postalCode;

    private String phone;
}