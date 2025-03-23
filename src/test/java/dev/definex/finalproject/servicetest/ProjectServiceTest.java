package dev.definex.finalproject.servicetest;

import dev.definex.finalproject.dto.ProjectDto;
import dev.definex.finalproject.entity.Department;
import dev.definex.finalproject.entity.Project;
import dev.definex.finalproject.enums.State;
import dev.definex.finalproject.exception.ProjectNotFoundException;
import dev.definex.finalproject.mapper.ProjectMapper;
import dev.definex.finalproject.repository.DepartmentRepository;
import dev.definex.finalproject.repository.ProjectRepository;
import dev.definex.finalproject.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectDto projectDto;
    private Project project;
    private Department department;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
         department = Department.builder().id(UUID.randomUUID()).name("IT Department").build();

        projectDto = ProjectDto.builder()
                .id(UUID.randomUUID())
                .title("Project Title")
                .description("Project Description")
                .state(State.Backlog)
                .departmentId(department.getId())
                .departmentName(department.getName())
                .build();

        project = Project.builder()
                .id(projectDto.getId())
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .state(projectDto.getState())
                .department(department)
                .userList(new ArrayList<>())
                .tasks(new ArrayList<>())
                .build();
    }

    @Test
    public void testSave() {
        when(projectMapper.toProject(projectDto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toProjectDto(project)).thenReturn(projectDto);
        ProjectDto savedProject = projectService.save(projectDto);
        assertNotNull(savedProject);
        assertEquals(savedProject.getId(), projectDto.getId());
        verify(projectRepository, times(1)).save(project);
    }
    @Test
    public void testFindAll() {
        List<Project> projects = List.of(project);
        when(projectRepository.findAll()).thenReturn(projects);
        when(projectMapper.toProjectDto(project)).thenReturn(projectDto);
        List<ProjectDto> foundProjects = projectService.findAll();
        assertNotNull(foundProjects);
        assertEquals(1, foundProjects.size());
        assertEquals(projectDto.getId(), foundProjects.get(0).getId());
    }

    @Test
    public void testFindNotDeleted() {
        List<Project> projects = List.of(project);
        when(projectRepository.findByIsDeletedFalse()).thenReturn(projects);
        when(projectMapper.toProjectDto(project)).thenReturn(projectDto);
        List<ProjectDto> foundProjects = projectService.findNotDeleted();
        assertNotNull(foundProjects);
        assertEquals(1, foundProjects.size());
        assertEquals(projectDto.getId(), foundProjects.get(0).getId());
    }

    @Test
    public void testUpdate() throws ProjectNotFoundException {
        when(projectRepository.findById(projectDto.getId())).thenReturn(Optional.of(project));
        when(departmentRepository.findById(projectDto.getDepartmentId())).thenReturn(Optional.of(department));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toProjectDto(project)).thenReturn(projectDto);
        ProjectDto updatedProject = projectService.update(projectDto);
        assertNotNull(updatedProject);
        assertEquals(projectDto.getTitle(), updatedProject.getTitle());
        verify(projectRepository).save(project);
    }

    @Test
    public void testUpdateThrowsProjectNotFoundException() {
        when(projectRepository.findById(projectDto.getId())).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.update(projectDto));
    }

    @Test
    public void testDelete() throws ProjectNotFoundException {
        when(projectRepository.existsById(projectDto.getId())).thenReturn(true);
        projectService.delete(projectDto.getId());
        verify(projectRepository).softDeleteById(projectDto.getId());
    }

    @Test
    public void testDeleteThrowsProjectNotFoundException() {
        when(projectRepository.existsById(projectDto.getId())).thenReturn(false);
        assertThrows(ProjectNotFoundException.class, () -> projectService.delete(projectDto.getId()));
    }

    @Test
    public void testFindById() throws ProjectNotFoundException {
        when(projectRepository.findById(projectDto.getId())).thenReturn(Optional.of(project));
        when(projectMapper.toProjectDto(project)).thenReturn(projectDto);
        ProjectDto foundProject = projectService.findById(projectDto.getId());
        assertNotNull(foundProject);
        assertEquals(projectDto.getId(), foundProject.getId());
    }

    @Test
    public void testFindByIdThrowsProjectNotFoundException() {
        when(projectRepository.findById(projectDto.getId())).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.findById(projectDto.getId()));
    }
}
