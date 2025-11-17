package com.scentalux.controller;

import com.scentalux.model.Role;
import com.scentalux.model.User;
import com.scentalux.repo.IUserRepo;
import com.scentalux.security.JwtRequest;
import com.scentalux.security.JwtResponse;
import com.scentalux.security.JwtTokenUtil;
import com.scentalux.security.JwtUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private IUserRepo userRepo;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
void login_ReturnsTokenAndUserData() throws Exception {

    // Arrange
    JwtRequest request = new JwtRequest("userTest", "1234");

    UserDetails userDetails = mock(UserDetails.class);
    when(jwtUserDetailsService.loadUserByUsername("userTest")).thenReturn(userDetails);

    when(jwtTokenUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");

    // Crear usuario simulado
    User user = new User();
    user.setUsername("userTest");
    user.setPassword("encodedPass");
    user.setEnabled(true);

    Role role = new Role();
    role.setIdRole(1);
    role.setName("ADMIN");
    role.setDescription("Administrador");

    // 👇 Aquí el cambio correcto
    user.setRoles(List.of(role));

    when(userRepo.findOneByUsername("userTest")).thenReturn(user);


    // Act
    ResponseEntity<JwtResponse> response = loginController.login(request);

    // Assert
    assertNotNull(response);
    assertEquals("fake-jwt-token", response.getBody().accessToken());
    assertEquals("userTest", response.getBody().username());
    assertEquals(List.of("ADMIN"), response.getBody().roles());

    verify(authenticationManager).authenticate(any());
    verify(jwtUserDetailsService).loadUserByUsername("userTest");
    verify(jwtTokenUtil).generateToken(userDetails);
    verify(userRepo).findOneByUsername("userTest");
}

}
