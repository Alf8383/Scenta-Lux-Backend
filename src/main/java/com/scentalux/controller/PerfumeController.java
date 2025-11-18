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

@RestController
@RequestMapping("/perfumes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PerfumeController {

    private final PerfumeService perfumeService;
    
    // Define a constant for the error key
    private static final String ERROR_KEY = "error"; 

    // ✅ Listar todos los perfumes
    @GetMapping
    public ResponseEntity<List<PerfumeDTO>> listar() {
        try {
            List<Perfume> perfumes = perfumeService.findAll();
            List<PerfumeDTO> perfumesDTO = perfumes.stream()
                    .map(PerfumeMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(perfumesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // ✅ Obtener perfume por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Integer id) {
        try {
            Perfume perfume = perfumeService.findById(id);
            return ResponseEntity.ok(Map.of("perfume", PerfumeMapper.toDTO(perfume)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR_KEY, e.getMessage()));  // Use constant here
        }
    }

    // ✅ Registrar nuevo perfume (desde frontend)
    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody PerfumeDTO dto) {
        try {
            Perfume perfume = PerfumeMapper.toEntity(dto);
            Perfume creado = perfumeService.save(perfume);
            return new ResponseEntity<>(PerfumeMapper.toDTO(creado), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(ERROR_KEY, e.getMessage()));  // Use constant here
        }
    }

    // ✅ Actualizar perfume existente
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> modificar(@PathVariable Integer id, @Valid @RequestBody PerfumeDTO dto) {
        try {
            Perfume existente = perfumeService.findById(id);
            Perfume actualizado = PerfumeMapper.updateEntityFromDTO(dto, existente);
            Perfume guardado = perfumeService.update(actualizado, id);
            return ResponseEntity.ok(Map.of("perfume", PerfumeMapper.toDTO(guardado)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(ERROR_KEY, e.getMessage()));  // Use constant here
        }
    }

    // ✅ Eliminar perfume
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            perfumeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR_KEY, e.getMessage()));  // Use constant here
        }
    }

    // ✅ Cambiar estado publicado
    @PutMapping("/{id}/publish")
    public ResponseEntity<Object> togglePublish(@PathVariable Integer id) {
        try {
            Perfume perfume = perfumeService.findById(id);
            perfume.setPublished(!perfume.isPublished());
            Perfume updated = perfumeService.update(perfume, id);
            return ResponseEntity.ok(PerfumeMapper.toDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR_KEY, e.getMessage()));  // Use constant here
        }
    }

    // ✅ Actualizar stock después de compra
    @PutMapping("/{id}/stock")
    public ResponseEntity<Object> updateStock(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        try {
            int quantitySold = body.getOrDefault("quantitySold", 0);
            Perfume perfume = perfumeService.findById(id);
            if (quantitySold > perfume.getStock()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(ERROR_KEY, "Stock insuficiente"));  // Use constant here
            }
            perfume.setStock(perfume.getStock() - quantitySold);
            Perfume updated = perfumeService.update(perfume, id);
            return ResponseEntity.ok(PerfumeMapper.toDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR_KEY, e.getMessage()));  // Use constant here
        }
    }
}
