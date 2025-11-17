package com.scentalux.controller;

import com.scentalux.model.User;
import com.scentalux.repo.IUserRepo;
import com.scentalux.security.JwtRequest;
import com.scentalux.security.JwtResponse;
import com.scentalux.security.JwtTokenUtil;
import com.scentalux.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final IUserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest req) throws Exception {
        authenticate(req.getUsername(), req.getPassword());

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(req.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Aquí usamos directamente findOneByUsername
        User user = userRepo.findOneByUsername(req.getUsername());

        // Convertimos lista de roles a nombres
        List<String> roles=user.getRoles().stream().map(role ->role.getName()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(token, user.getUsername(), roles));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
