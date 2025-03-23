package dev.definex.finalproject.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.definex.finalproject.controller.ProjectController;
import dev.definex.finalproject.dto.ProjectDto;

import dev.definex.finalproject.security.SecurityConfig;
import dev.definex.finalproject.service.ProjectService;
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

@WebMvcTest(ProjectController.class)
@Import({SecurityConfig.class, TestConfig.class})
@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private ProjectService projectService;


    private ProjectDto projectDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        projectDto = ProjectDto.builder()
                .id(UUID.randomUUID())
                .title("New Project")
                .description("Project description")
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_ShouldReturnOk() throws Exception {
        when(projectService.findAll()).thenReturn(List.of(projectDto));

        mockMvc.perform(get("/api/project/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value(projectDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findNotDeleted_ShouldReturnOk() throws Exception {
        when(projectService.findNotDeleted()).thenReturn(List.of(projectDto));

        mockMvc.perform(get("/api/project/findNotDeleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value(projectDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_ShouldReturnOk() throws Exception {
        when(projectService.findById(projectDto.getId())).thenReturn(projectDto);

        mockMvc.perform(get("/api/project/find/" + projectDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(projectDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_ShouldReturnOk() throws Exception {
        when(projectService.save(any(ProjectDto.class))).thenReturn(projectDto);

        mockMvc.perform(post("/api/project/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(projectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(projectDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void save_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/project/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(projectDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_ShouldReturnOk() throws Exception {
        when(projectService.update(any(ProjectDto.class))).thenReturn(projectDto);

        mockMvc.perform(put("/api/project/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(projectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(projectDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void update_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/project/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(projectDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_ShouldReturnOk() throws Exception {
        doNothing().when(projectService).delete(projectDto.getId());

        mockMvc.perform(put("/api/project/delete/" + projectDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void delete_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(put("/api/project/delete/" + projectDto.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccess_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/project/findAll"))
                .andExpect(status().isForbidden());
    }
}
