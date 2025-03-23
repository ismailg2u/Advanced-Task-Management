package dev.definex.finalproject.controllertest;

import dev.definex.finalproject.repository.DepartmentRepository;
import dev.definex.finalproject.repository.ProjectRepository;
import dev.definex.finalproject.repository.TaskRepository;
import dev.definex.finalproject.repository.UserRepository;
import dev.definex.finalproject.service.DepartmentService;
import dev.definex.finalproject.service.ProjectService;
import dev.definex.finalproject.service.TaskService;
import dev.definex.finalproject.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public DepartmentService departmentService() {
        return Mockito.mock(DepartmentService.class);
    }

    @Bean
    public ProjectService projectService() {
        return Mockito.mock(ProjectService.class);
    }

    @Bean
    public TaskService taskService() {
        return Mockito.mock(TaskService.class);
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public DepartmentRepository departmentRepository() {
        return Mockito.mock(DepartmentRepository.class);
    }

    @Bean
    public ProjectRepository projectRepository() {
        return Mockito.mock(ProjectRepository.class);
    }

    @Bean
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class);
    }
}
