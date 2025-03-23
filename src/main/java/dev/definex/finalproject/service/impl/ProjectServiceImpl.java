package dev.definex.finalproject.service.impl;

import dev.definex.finalproject.dto.DepartmentDto;
import dev.definex.finalproject.dto.ProjectDto;
import dev.definex.finalproject.entity.Department;
import dev.definex.finalproject.entity.Project;
import dev.definex.finalproject.exception.ProjectNotFoundException;
import dev.definex.finalproject.mapper.DepartmentMapper;
import dev.definex.finalproject.mapper.ProjectMapper;
import dev.definex.finalproject.mapper.TaskMapper;
import dev.definex.finalproject.mapper.UserMapper;
import dev.definex.finalproject.repository.DepartmentRepository;
import dev.definex.finalproject.repository.ProjectRepository;
import dev.definex.finalproject.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper
  ,UserMapper userMapper,TaskMapper taskMapper
    ,DepartmentRepository departmentRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
        this.departmentRepository = departmentRepository;

    }



    @Override
    public ProjectDto save(ProjectDto projectDto) {
        Project savedProject = projectRepository.save(projectMapper.toProject(projectDto));
        return projectMapper.toProjectDto(savedProject);
    }

    @Override
    public List<ProjectDto> findAll() {
        List<ProjectDto> projectDtoList;
        List<Project> projectList = projectRepository.findAll();
        projectDtoList = projectList.stream().
                map(project -> projectMapper.toProjectDto((project))).toList();
        return projectDtoList;
    }

    @Override
    public List<ProjectDto> findNotDeleted() {
        List<ProjectDto> projectDtoList;
        List<Project> projectList = projectRepository.findByIsDeletedFalse();
        projectDtoList = projectList.stream().
                map(project -> projectMapper.toProjectDto((project))).toList();
        return projectDtoList;
    }

    @Override
    public ProjectDto update(ProjectDto projectDto) throws ProjectNotFoundException{
        Project project = projectRepository.findById(projectDto.getId()).orElseThrow(ProjectNotFoundException::new);
        project.setDepartment(departmentRepository.findById(projectDto.getDepartmentId()).orElseThrow());
        project.setState(projectDto.getState());
        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.getTasks().clear();
        if (projectDto.getTasks() != null) {
            project.getTasks().addAll(projectDto.getTasks().stream()
                    .map(taskMapper::toTask)
                    .collect(Collectors.toList()));
        }
        project.getUserList().clear();
        if (projectDto.getUserList() != null) {
            project.getUserList().addAll(projectDto.getUserList().stream()
                    .map(userMapper::toUser)
                    .collect(Collectors.toList()));
        }
        projectRepository.save(project);
        return projectMapper.toProjectDto(project);
    }

    @Override
    public void delete(UUID uuid) throws ProjectNotFoundException{
        if(projectRepository.existsById(uuid)){
            projectRepository.softDeleteById(uuid);
        }
        else
           throw new ProjectNotFoundException();

    }

    @Override
    public ProjectDto findById(UUID uuid) throws ProjectNotFoundException {
        return projectMapper.toProjectDto(projectRepository.findById(uuid).orElseThrow(ProjectNotFoundException::new));
    }
}
