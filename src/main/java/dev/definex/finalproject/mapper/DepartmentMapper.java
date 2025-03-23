package dev.definex.finalproject.mapper;


import dev.definex.finalproject.dto.DepartmentDto;
import dev.definex.finalproject.dto.ProjectDto;
import dev.definex.finalproject.entity.Department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class DepartmentMapper {
    @Autowired
    @Lazy
    private ProjectMapper projectMapper;

    public DepartmentDto toDepartmentDto(Department department) {
        if (department == null) {
            return null;
        }
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .projects(department.getProjects() != null ? department.getProjects().stream()
                        .map(projectMapper::toProjectDto)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    public Department toDepartment(DepartmentDto departmentDto) {
        if (departmentDto == null) {
            return null;
        }
        return Department.builder()
                .id(departmentDto.getId())
                .name(departmentDto.getName())
                .projects(departmentDto.getProjects() != null ? departmentDto.getProjects().stream()
                        .map(projectMapper::toProject)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();





    }}
