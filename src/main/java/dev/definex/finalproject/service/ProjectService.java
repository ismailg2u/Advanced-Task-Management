package dev.definex.finalproject.service;

import dev.definex.finalproject.dto.ProjectDto;
import dev.definex.finalproject.exception.ProjectNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectDto save(ProjectDto projectDto);
    List<ProjectDto> findAll();
    List<ProjectDto> findNotDeleted();
    ProjectDto update(ProjectDto projectDto) throws ProjectNotFoundException;
    void delete(UUID uuid) throws ProjectNotFoundException;
    ProjectDto findById(UUID uuid) throws ProjectNotFoundException;
}
