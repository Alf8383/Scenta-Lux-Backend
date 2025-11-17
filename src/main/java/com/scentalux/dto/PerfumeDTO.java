package com.scentalux.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeDTO {

    private String id; // string para coincidir con el frontend

    @NotBlank(message = "El nombre del perfume es obligatorio")
    private String name;

    @NotBlank(message = "La marca es obligatoria")
    private String brand;

    @NotNull(message = "El precio es obligatorio")
    private Double price;

    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    private String imageUrl;
    private String description;

    @NotBlank(message = "La categoría es obligatoria")
    private String category; // "Para Él" | "Para Ella" | "Unisex"

    private boolean published = true;
    private List<String> notes;
     private LocalDateTime createdAt; // ✅ NUEVO CAMPO
}
