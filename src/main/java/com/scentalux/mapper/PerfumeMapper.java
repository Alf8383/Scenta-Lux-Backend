package com.scentalux.mapper;

import com.scentalux.dto.PerfumeDTO;
import com.scentalux.model.Perfume;

public class PerfumeMapper {

    // Convertir modelo → DTO (para enviar al frontend)
    public static PerfumeDTO toDTO(Perfume perfume) {
        return new PerfumeDTO(
                perfume.getId().toString(),
                perfume.getName(),
                perfume.getBrand(),
                perfume.getPrice(),
                perfume.getStock(),
                perfume.getImageUrl(),
                perfume.getDescription(),
                mapCategoryToFrontend(perfume.getCategory()),
                perfume.isPublished(),
                perfume.getNotes(),
                perfume.getCreatedAt()
        );
    }

    // Convertir DTO → modelo (para guardar en BD)
    public static Perfume toEntity(PerfumeDTO dto) {
        Perfume perfume = new Perfume();
        if (dto.getId() != null) {
            perfume.setId(Integer.parseInt(dto.getId()));
        }
        perfume.setName(dto.getName());
        perfume.setBrand(dto.getBrand());
        perfume.setPrice(dto.getPrice());
        perfume.setStock(dto.getStock());
        perfume.setImageUrl(dto.getImageUrl());
        perfume.setDescription(dto.getDescription());
        perfume.setCategory(mapCategoryToBackend(dto.getCategory()));
        perfume.setPublished(dto.isPublished());
        perfume.setNotes(dto.getNotes());
        perfume.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : perfume.getCreatedAt());
        return perfume;
    }

    // Actualizar un perfume existente con datos del DTO
    public static Perfume updateEntityFromDTO(PerfumeDTO dto, Perfume perfume) {
        perfume.setName(dto.getName());
        perfume.setBrand(dto.getBrand());
        perfume.setPrice(dto.getPrice());
        perfume.setStock(dto.getStock());
        perfume.setImageUrl(dto.getImageUrl());
        perfume.setDescription(dto.getDescription());
        perfume.setCategory(mapCategoryToBackend(dto.getCategory()));
        perfume.setPublished(dto.isPublished());
        perfume.setNotes(dto.getNotes());
        return perfume;
    }

    // Mapear categoría para mostrar en frontend
    private static String mapCategoryToFrontend(String backendCategory) {
        switch (backendCategory) {
            case "PARA_EL": return "Para Él";
            case "PARA_ELLA": return "Para Ella";
            case "UNISEX": return "Unisex";
            default: return "Unisex";
        }
    }

    // Mapear categoría de frontend → backend
    private static String mapCategoryToBackend(String frontendCategory) {
        switch (frontendCategory) {
            case "Para Él": return "PARA_EL";
            case "Para Ella": return "PARA_ELLA";
            case "Unisex": return "UNISEX";
            default: return "UNISEX";
        }
    }
}
