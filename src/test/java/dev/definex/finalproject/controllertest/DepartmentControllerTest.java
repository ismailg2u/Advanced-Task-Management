package dev.definex.finalproject.controllertest;

import dev.definex.finalproject.controller.DepartmentController;
import dev.definex.finalproject.dto.DepartmentDto;
import dev.definex.finalproject.security.SecurityConfig;
import dev.definex.finalproject.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DepartmentController.class)
@Import({SecurityConfig.class, TestConfig.class})
@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;


    private DepartmentDto departmentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        departmentDto = DepartmentDto.builder()
                .id(UUID.randomUUID())
                .name("HR")
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_ShouldReturnOk() throws Exception {
        when(departmentService.findAll()).thenReturn(List.of(departmentDto));

        mockMvc.perform(get("/api/department/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(departmentDto.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findNotDeleted_ShouldReturnOk() throws Exception {
        when(departmentService.findNotDeleted()).thenReturn(List.of(departmentDto));

        mockMvc.perform(get("/api/department/findNotDeleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(departmentDto.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_ShouldReturnOk() throws Exception {
        when(departmentService.findById(departmentDto.getId())).thenReturn(departmentDto);

        mockMvc.perform(get("/api/department/find/" + departmentDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(departmentDto.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_ShouldReturnOk() throws Exception {
        when(departmentService.save(any(DepartmentDto.class))).thenReturn(departmentDto);

        mockMvc.perform(post("/api/department/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(departmentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(departmentDto.getName()));
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void save_ShouldReturnForbidden() throws Exception {
        when(departmentService.save(any(DepartmentDto.class))).thenReturn(departmentDto);

        mockMvc.perform(post("/api/department/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(departmentDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_ShouldReturnOk() throws Exception {
        when(departmentService.update(any(DepartmentDto.class))).thenReturn(departmentDto);

        mockMvc.perform(put("/api/department/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(departmentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(departmentDto.getName()));
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void update_ShouldReturnForbidden() throws Exception {
        when(departmentService.update(any(DepartmentDto.class))).thenReturn(departmentDto);

        mockMvc.perform(put("/api/department/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(departmentDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_ShouldReturnOk() throws Exception {
        doNothing().when(departmentService).delete(departmentDto.getId());

        mockMvc.perform(put("/api/department/delete/" + departmentDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void delete_ShouldReturnForbidden() throws Exception {
        doNothing().when(departmentService).delete(departmentDto.getId());

        mockMvc.perform(put("/api/department/delete/" + departmentDto.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccess_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/department/findAll"))
                .andExpect(status().isForbidden());
    }
}
