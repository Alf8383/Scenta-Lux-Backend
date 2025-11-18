package com.scentalux.controller;

import com.scentalux.dto.RolDTO;
import com.scentalux.model.Role;
import com.scentalux.service.RolService;
import com.scentalux.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// @PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RolController {

    private final RolService service;
    private final ModelMapper modelMapper;

    // Obtener todos los roles
    @GetMapping
    public ResponseEntity<List<RolDTO>> findAll() {
        List<RolDTO> list = service.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    // Obtener un rol por su ID
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> findById(@PathVariable("id") Integer id) {
        Role role = service.findById(id);
        if (role == null) {
            throw new ResourceNotFoundException("Role not found with id: " + id);  // Custom exception
        }
        return ResponseEntity.ok(convertToDto(role));
    }

    // Crear un nuevo rol
    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody RolDTO dto) {
        Role obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getIdRole())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // Actualizar un rol existente
    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> update(@PathVariable("id") Integer id, @Valid @RequestBody RolDTO dto) {
        Role updated = service.update(convertToEntity(dto), id);
        if (updated == null) {
            throw new ResourceNotFoundException("Role not found with id: " + id);  // Custom exception
        }
        return ResponseEntity.ok(convertToDto(updated));
    }

    // Eliminar un rol
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);  // No need to capture return value if it's void
        return ResponseEntity.noContent().build();  // Return 204 No Content
    }

    // Paginación de roles
    @GetMapping("/pageable")
    public ResponseEntity<Page<Role>> listPage(Pageable pageable) {
        Page<Role> page = service.listPage(pageable);
        return ResponseEntity.ok(page);
    }

    // Convertir entidad → DTO
    private RolDTO convertToDto(Role obj) {
        return modelMapper.map(obj, RolDTO.class);
    }

    // Convertir DTO → entidad
    private Role convertToEntity(RolDTO dto) {
        return modelMapper.map(dto, Role.class);
    }
}
