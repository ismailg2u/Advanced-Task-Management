package dev.definex.finalproject.mapper;

import dev.definex.finalproject.dto.TaskDto;
import dev.definex.finalproject.entity.Task;
import dev.definex.finalproject.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    @Lazy
    private ProjectMapper projectMapper;

    public TaskDto toTaskDto(Task task) {
        if (task == null) {
            return null;
        }
        return TaskDto.builder()
                .id(task.getId())
                .priority(task.getPriority())
                .state(task.getState())
                .title(task.getTitle())
                .userStoryDescription(task.getUserStoryDescription())
                .acceptanceCriteria(task.getAcceptanceCriteria())
                .reason(task.getReason())
                .userList(task.getUserList() != null ? task.getUserList().stream()
                        .map(userMapper::toUserDto)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .comments(task.getComments() != null ? new ArrayList<>(task.getComments()) : new ArrayList<>())
                .files(task.getFiles() != null ? new ArrayList<>(task.getFiles()) : new ArrayList<>())
                .projectId(task.getProject().getId())
                .build();
    }

    public Task toTask(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        return Task.builder()
                .id(taskDto.getId())
                .priority(taskDto.getPriority())
                .state(taskDto.getState())
                .title(taskDto.getTitle())
                .userStoryDescription(taskDto.getUserStoryDescription())
                .acceptanceCriteria(taskDto.getAcceptanceCriteria())
                .reason(taskDto.getReason())
                .userList(taskDto.getUserList() != null ? taskDto.getUserList().stream()
                        .map(userMapper::toUser)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .comments(taskDto.getComments() != null ? new ArrayList<>(taskDto.getComments()) : new ArrayList<>())
                .files(taskDto.getFiles() != null ? new ArrayList<>(taskDto.getFiles()) : new ArrayList<>())
                .project(projectRepository.findById(taskDto.getProjectId()).orElseThrow())
                .build();
    }
}
