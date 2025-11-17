package com.scentalux.controller;

import com.scentalux.dto.UserRolDTO;
import com.scentalux.model.Role;
import com.scentalux.model.User;
import com.scentalux.service.RolService;
import com.scentalux.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserRoleController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    // 1. Asignar un rol a un usuario
    @PostMapping
    public ResponseEntity<String> assignRoleToUser(@RequestBody UserRolDTO dto) throws Exception {
        User user = usuarioService.findById(dto.getUserId());
        Role role = rolService.findById(dto.getRoleId());

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            usuarioService.save(user);
            return ResponseEntity.ok("Rol asignado exitosamente al usuario.");
        } else {
            return ResponseEntity.badRequest().body("El usuario ya tiene este rol.");
        }
    }

    // 2. Eliminar un rol de un usuario
    @DeleteMapping
    public ResponseEntity<String> removeRoleFromUser(
            @RequestParam Integer userId,
            @RequestParam Integer roleId) throws Exception {

        User user = usuarioService.findById(userId);
        Role role = rolService.findById(roleId);

        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            usuarioService.save(user);
            return ResponseEntity.ok("Rol eliminado exitosamente del usuario.");
        } else {
            return ResponseEntity.badRequest().body("El usuario no tiene este rol.");
        }
    }

    // 3. Listar los roles de un usuario
    @GetMapping("/{userId}")
    public ResponseEntity<List<Role>> getRolesByUser(@PathVariable Integer userId) throws Exception {
        User user = usuarioService.findById(userId);
        return ResponseEntity.ok(user.getRoles());
    }
}
