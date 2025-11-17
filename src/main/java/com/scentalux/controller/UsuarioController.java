package com.scentalux.controller;

import com.scentalux.dto.UsuarioDTO;
import com.scentalux.model.Role;
import com.scentalux.model.User;
import com.scentalux.service.RolService;
import com.scentalux.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // ✅ Listar usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() throws Exception {
        List<User> usuarios = usuarioService.findAll();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(user -> mapper.map(user, UsuarioDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDTO);
    }

    // ✅ Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable("id") Integer id) {
        try {
            User user = usuarioService.findById(id);
            UsuarioDTO dto = mapper.map(user, UsuarioDTO.class);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Registrar usuario con rol CLIENTE por defecto
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioDTO dto) {
        try {
            // 1️⃣ Mapeamos DTO a entidad
            User user = mapper.map(dto, User.class);

            // 2️⃣ Encriptamos contraseña
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

            // 3️⃣ Habilitamos usuario por defecto
            user.setEnabled(true);

            // 4️⃣ Asignamos rol CLIENTE por defecto
            Role clienteRole = rolService.findAll().stream()
                    .filter(r -> r.getName().equalsIgnoreCase("CLIENTE"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("El rol CLIENTE no existe en la base de datos"));
            user.getRoles().add(clienteRole);

            // 5️⃣ Guardamos en la BD
            User creado = usuarioService.save(user);

            UsuarioDTO respuesta = mapper.map(creado, UsuarioDTO.class);
            return new ResponseEntity<>(respuesta, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Modificar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> modificar(@PathVariable("id") Integer id, @Valid @RequestBody UsuarioDTO dto) {
        try {
            User existente = usuarioService.findById(id);

            existente.setUsername(dto.getUsername());
            existente.setPassword(passwordEncoder.encode(dto.getPassword()));
            existente.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : existente.getEnabled());

            User actualizado = usuarioService.update(existente, id);
            UsuarioDTO respuesta = mapper.map(actualizado, UsuarioDTO.class);
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Integer id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
