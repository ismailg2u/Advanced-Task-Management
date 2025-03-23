package dev.definex.finalproject.servicetest;



import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import dev.definex.finalproject.dto.TaskDto;
import dev.definex.finalproject.entity.Project;
import dev.definex.finalproject.entity.Task;
import dev.definex.finalproject.entity.User;
import dev.definex.finalproject.enums.Priority;
import dev.definex.finalproject.enums.State;
import dev.definex.finalproject.enums.UserRole;
import dev.definex.finalproject.exception.ProjectNotFoundException;
import dev.definex.finalproject.exception.TaskNotFoundException;
import dev.definex.finalproject.exception.UnauthorizedActionException;
import dev.definex.finalproject.exception.UserNotFoundException;
import dev.definex.finalproject.mapper.TaskMapper;
import dev.definex.finalproject.mapper.UserMapper;
import dev.definex.finalproject.repository.ProjectRepository;
import dev.definex.finalproject.repository.TaskRepository;
import dev.definex.finalproject.repository.UserRepository;
import dev.definex.finalproject.service.impl.TaskServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import dev.definex.finalproject.tool.TaskStateValidator;
import java.util.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskStateValidator taskStateValidator;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskDto taskDto;
    private Task task;
    private Project project;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        project = Project.builder()
                .id(UUID.randomUUID())
                .title("Test Project")
                .build();

        user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .userRole(UserRole.Team_Member)
                .build();

        taskDto = TaskDto.builder()
                .id(UUID.randomUUID())
                .title("Test Task")
                .priority(Priority.High)
                .state(State.Backlog)
                .userStoryDescription("Task Description")
                .acceptanceCriteria("Criteria")
                .userList(new ArrayList<>())
                .comments(new ArrayList<>())
                .files(new ArrayList<>())
                .projectId(project.getId())
                .reason("Some reason")
                .build();

        task = Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .priority(taskDto.getPriority())
                .state(taskDto.getState())
                .userStoryDescription(taskDto.getUserStoryDescription())
                .acceptanceCriteria(taskDto.getAcceptanceCriteria())
                .userList(new ArrayList<>())
                .comments(new ArrayList<>())
                .files(new ArrayList<>())
                .project(project)
                .build();
    }

    @Test
    public void testSave() {
        when(taskMapper.toTask(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

        TaskDto savedTask = taskService.save(taskDto);

        assertNotNull(savedTask);
        assertEquals(taskDto.getTitle(), savedTask.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    public void testFindAll() {
        List<Task> taskList = List.of(task);
        when(taskRepository.findAll()).thenReturn(taskList);
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

        List<TaskDto> result = taskService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskDto.getTitle(), result.get(0).getTitle());
    }

    @Test
    public void testFindNotDeleted() {
        List<Task> taskList = List.of(task);
        when(taskRepository.findByIsDeletedFalse()).thenReturn(taskList);
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

        List<TaskDto> result = taskService.findNotDeleted();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(taskDto.getTitle(), result.get(0).getTitle());
    }

    @Test
    public void testUpdate() {
        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(task));
        when(projectRepository.findById(taskDto.getProjectId())).thenReturn(Optional.of(project));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        TaskDto updatedTask = taskService.update(taskDto);

        assertNotNull(updatedTask);
        assertEquals(taskDto.getTitle(), updatedTask.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    public void testUpdateUnauthorizedActionException() {
        user.setUserRole(UserRole.Team_Member);
        task.setTitle("Different Title");

        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(task));
        when(projectRepository.findById(taskDto.getProjectId())).thenReturn(Optional.of(project));
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedActionException.class, () -> taskService.update(taskDto));
    }

    @Test
    public void testDelete() {
        when(taskRepository.existsById(taskDto.getId())).thenReturn(true);

        taskService.delete(taskDto.getId());

        verify(taskRepository).softDeleteById(taskDto.getId());
    }

    @Test
    public void testDeleteTaskNotFound() {
        when(taskRepository.existsById(taskDto.getId())).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.delete(taskDto.getId()));
    }

    @Test
    public void testFindById() {
        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(task));
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.findById(taskDto.getId());

        assertNotNull(result);
        assertEquals(taskDto.getTitle(), result.getTitle());
    }

    @Test
    public void testFindByIdTaskNotFound() {
        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(taskDto.getId()));
    }


    @Test
    public void testUpdateUnauthorizedUserStoryDescriptionChange() {
        user.setUserRole(UserRole.Team_Member);
        task.setUserStoryDescription("Different Description");

        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(task));
        when(projectRepository.findById(taskDto.getProjectId())).thenReturn(Optional.of(project));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedActionException.class, () -> taskService.update(taskDto));
    }
    @Test
    public void testUpdateUnauthorizedPriorityChange() {
        user.setUserRole(UserRole.Team_Member);
        task.setPriority(Priority.Low);

        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(task));
        when(projectRepository.findById(taskDto.getProjectId())).thenReturn(Optional.of(project));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedActionException.class, () -> taskService.update(taskDto));
    }

    @Test
    public void testUpdateAsAuthorized() {
        user.setUserRole(UserRole.ADMIN);

        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.of(task));
        when(projectRepository.findById(taskDto.getProjectId())).thenReturn(Optional.of(project));
        when(taskMapper.toTaskDto(task)).thenReturn(taskDto);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");

        TaskDto updatedTask = taskService.update(taskDto);

        assertNotNull(updatedTask);
        assertEquals(taskDto.getTitle(), updatedTask.getTitle());
        verify(taskRepository).save(task);
    }



}

