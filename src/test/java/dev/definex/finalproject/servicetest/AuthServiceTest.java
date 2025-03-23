package dev.definex.finalproject.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.definex.finalproject.entity.User;
import dev.definex.finalproject.enums.UserRole;
import dev.definex.finalproject.repository.UserRepository;
import dev.definex.finalproject.service.impl.AuthService;
import dev.definex.finalproject.security.JwtService;
import dev.definex.finalproject.dto.request.LoginRequest;
import dev.definex.finalproject.dto.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("John");
        registerRequest.setSurname("Doe");
        registerRequest.setEmail("johndoe@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setUserRole(UserRole.Project_Manager);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("johndoe@example.com");
        loginRequest.setPassword("password123");

        user = User.builder()
                .name("John")
                .surname("Doe")
                .email("johndoe@example.com")
                .password("encodedPassword")
                .userRole(UserRole.Project_Manager)
                .build();
    }

    @Test
    void register_ShouldReturnJwtToken() {
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn("mockedJwtToken");

        String token = authService.register(registerRequest);

        assertNotNull(token);
        assertEquals("mockedJwtToken", token);
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any());
    }

    @Test
    void authenticate_ShouldReturnJwtToken() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("mockedJwtToken");
        doAnswer(invocation -> null).when(authenticationManager).authenticate(any());


        String token = authService.authenticate(loginRequest);

        assertNotNull(token);
        assertEquals("mockedJwtToken", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(any());
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.authenticate(loginRequest));
    }
}
