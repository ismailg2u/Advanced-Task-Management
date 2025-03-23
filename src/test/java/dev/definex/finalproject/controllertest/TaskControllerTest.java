package dev.definex.finalproject.controllertest;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.definex.finalproject.controller.TaskController;
import dev.definex.finalproject.dto.TaskDto;
import dev.definex.finalproject.security.SecurityConfig;
import dev.definex.finalproject.service.TaskService;
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

@WebMvcTest(TaskController.class)
@Import({SecurityConfig.class, TestConfig.class})
@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private TaskService taskService;


    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        taskDto = TaskDto.builder()
                .id(UUID.randomUUID())
                .title("New Task")
                .userStoryDescription("Task description")
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_ShouldReturnOk() throws Exception {
        when(taskService.findAll()).thenReturn(List.of(taskDto));

        mockMvc.perform(get("/api/task/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value(taskDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findNotDeleted_ShouldReturnOk() throws Exception {
        when(taskService.findNotDeleted()).thenReturn(List.of(taskDto));

        mockMvc.perform(get("/api/task/findNotDeleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value(taskDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_ShouldReturnOk() throws Exception {
        when(taskService.findById(taskDto.getId())).thenReturn(taskDto);

        mockMvc.perform(get("/api/task/find/" + taskDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_ShouldReturnOk() throws Exception {
        when(taskService.save(any(TaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(post("/api/task/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()));
    }

    @Test
    @WithMockUser(roles = "Team_Member")
    void save_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/task/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_ShouldReturnOk() throws Exception {
        when(taskService.update(any(TaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(put("/api/task/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()));
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_ShouldReturnOk() throws Exception {
        doNothing().when(taskService).delete(taskDto.getId());

        mockMvc.perform(put("/api/task/delete/" + taskDto.getId()))
                .andExpect(status().isOk());
    }



    @Test
    void unauthorizedAccess_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/task/findAll"))
                .andExpect(status().isForbidden());
    }
}
