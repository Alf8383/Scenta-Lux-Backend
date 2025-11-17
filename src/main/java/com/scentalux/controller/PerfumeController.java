package com.scentalux.controller;

import com.scentalux.dto.PerfumeDTO;
import com.scentalux.mapper.PerfumeMapper;
import com.scentalux.model.Perfume;
import com.scentalux.service.PerfumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/perfumes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PerfumeController {

    private final PerfumeService perfumeService;

    // ✅ Listar todos los perfumes
    @GetMapping
public ResponseEntity<?> listar() {
    try {
        List<Perfume> perfumes = perfumeService.findAll();
        List<PerfumeDTO> perfumesDTO = perfumes.stream()
                .map(PerfumeMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(perfumesDTO);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al listar perfumes: " + e.getMessage()));
    }
}


    // ✅ Obtener perfume por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            Perfume perfume = perfumeService.findById(id);
            return ResponseEntity.ok(PerfumeMapper.toDTO(perfume));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Registrar nuevo perfume (desde frontend)
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody PerfumeDTO dto) {
        try {
            Perfume perfume = PerfumeMapper.toEntity(dto);
            Perfume creado = perfumeService.save(perfume);
            return new ResponseEntity<>(PerfumeMapper.toDTO(creado), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Actualizar perfume existente
    @PutMapping("/{id}")
    public ResponseEntity<?> modificar(@PathVariable Integer id, @Valid @RequestBody PerfumeDTO dto) {
        try {
            Perfume existente = perfumeService.findById(id);
            Perfume actualizado = PerfumeMapper.updateEntityFromDTO(dto, existente);
            Perfume guardado = perfumeService.update(actualizado, id);
            return ResponseEntity.ok(PerfumeMapper.toDTO(guardado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Eliminar perfume
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            perfumeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Cambiar estado publicado
    @PutMapping("/{id}/publish")
    public ResponseEntity<?> togglePublish(@PathVariable Integer id) {
        try {
            Perfume perfume = perfumeService.findById(id);
            perfume.setPublished(!perfume.isPublished());
            Perfume updated = perfumeService.update(perfume, id);
            return ResponseEntity.ok(PerfumeMapper.toDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Actualizar stock después de compra
    @PutMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        try {
            int quantitySold = body.getOrDefault("quantitySold", 0);
            Perfume perfume = perfumeService.findById(id);
            if (quantitySold > perfume.getStock()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Stock insuficiente"));
            }
            perfume.setStock(perfume.getStock() - quantitySold);
            Perfume updated = perfumeService.update(perfume, id);
            return ResponseEntity.ok(PerfumeMapper.toDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
