package com.scentalux.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "perfumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String brand;
    private double price;
    private int stock;
    private String imageUrl;
    private String description;

    // Se guarda en BD como: PARA_EL, PARA_ELLA o UNISEX
    private String category;

    private boolean published = true;

    @ElementCollection
    @CollectionTable(name = "perfume_notes", joinColumns = @JoinColumn(name = "perfume_id"))
    @Column(name = "note")
    private List<String> notes;
    // 👉 MUY IMPORTANTE: debe guardarse automáticamente al crear
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
