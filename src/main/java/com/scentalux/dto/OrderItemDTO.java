package com.scentalux.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    @NotNull(message = "El ID del perfume es obligatorio")
    private Integer perfumeId;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer quantity;
}