package dev.definex.finalproject.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.definex.finalproject.controller.AuthController;
import dev.definex.finalproject.dto.request.LoginRequest;
import dev.definex.finalproject.dto.request.RegisterRequest;
import dev.definex.finalproject.service.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;


    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        registerRequest = new RegisterRequest();
        registerRequest.setName("John");
        registerRequest.setSurname("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("securePassword");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("securePassword");
    }

    @Test
    void register_ShouldReturnJwtToken() throws Exception {
        String fakeToken = "fake-jwt-token";
        when(authService.register(any(RegisterRequest.class))).thenReturn(fakeToken);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(fakeToken));
    }

    @Test
    void login_ShouldReturnJwtToken() throws Exception {
        String fakeToken = "fake-jwt-token";
        when(authService.authenticate(any(LoginRequest.class))).thenReturn(fakeToken);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(fakeToken));
    }
}
