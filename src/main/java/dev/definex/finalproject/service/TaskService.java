package dev.definex.finalproject.service;

import dev.definex.finalproject.dto.ProjectDto;
import dev.definex.finalproject.dto.TaskDto;
import dev.definex.finalproject.exception.TaskNotFoundException;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskDto save(TaskDto taskDto);
    List<TaskDto> findAll();
    List<TaskDto> findNotDeleted();
    TaskDto update(TaskDto taskDto) throws TaskNotFoundException;
    void delete(UUID uuid) throws TaskNotFoundException;
    TaskDto findById(UUID uuid) throws TaskNotFoundException;


}
