package dev.definex.finalproject.mapper;

import dev.definex.finalproject.dto.ProjectDto;
import dev.definex.finalproject.entity.Project;
import dev.definex.finalproject.repository.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    @Lazy
    private DepartmentMapper departmentMapper;

    public ProjectDto toProjectDto(Project project) {
        if (project == null) {
            return null;
        }
        return ProjectDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .state(project.getState())
                .userList(project.getUserList() != null ? project.getUserList().stream()
                        .map(userMapper::toUserDto)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .departmentId(project.getDepartment().getId())
                .departmentName(project.getDepartment().getName())
                .tasks(project.getTasks() != null ? project.getTasks().stream()
                        .map(taskMapper::toTaskDto)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    public Project toProject(ProjectDto projectDto) {
        if (projectDto == null) {
            return null;
        }
        return Project.builder()
                .id(projectDto.getId())
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .state(projectDto.getState())
                .userList(projectDto.getUserList() != null ? projectDto.getUserList().stream()
                        .map(userMapper::toUser)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .department(departmentRepository.findById(projectDto.getDepartmentId()).orElseThrow())
                .tasks(projectDto.getTasks() != null ? projectDto.getTasks().stream()
                        .map(taskMapper::toTask)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
