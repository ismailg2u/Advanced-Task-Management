package dev.definex.finalproject.controllertest;



import com.fasterxml.jackson.databind.ObjectMapper;
import dev.definex.finalproject.controller.UserController;
import dev.definex.finalproject.dto.UserDto;
import dev.definex.finalproject.enums.UserRole;
import dev.definex.finalproject.security.SecurityConfig;
import dev.definex.finalproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, TestConfig.class})
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserDto userDto;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userDto = UserDto.builder()
                .id(UUID.randomUUID())
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .userRole(UserRole.ADMIN)
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_ShouldReturnOk() throws Exception {
        when(userService.findAll()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/api/user/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void findNotDeleted_ShouldReturnOk() throws Exception {
        when(userService.findByDeleted()).thenReturn(List.of(userDto));


        mockMvc.perform(get("/api/user/findNotDeleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_ShouldReturnOk() throws Exception {
        when(userService.findById(userDto.getId())).thenReturn(userDto);


        mockMvc.perform(get("/api/user/find/"+userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_ShouldReturnOk() throws Exception {
        when(userService.save(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }
    @Test
    @WithMockUser(roles = "Team_Member")
    void save_ShouldReturnForbidden() throws Exception {
        when(userService.save(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_ShouldReturnOk() throws Exception {
        when(userService.update(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userDto.getName()));
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void update_ShouldReturnForbidden() throws Exception {
        when(userService.update(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_ShouldReturnOk() throws Exception {
        doNothing().when(userService).delete(userDto.getId());

        mockMvc.perform(put("/api/user/delete/" + userDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void delete_ShouldReturnForbidden() throws Exception {
        doNothing().when(userService).delete(userDto.getId());

        mockMvc.perform(put("/api/user/delete/" + userDto.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccess_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/user/findAll"))
                .andExpect(status().isForbidden());
    }
}

