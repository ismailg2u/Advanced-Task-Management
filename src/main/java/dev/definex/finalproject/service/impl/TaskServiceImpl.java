package dev.definex.finalproject.service.impl;

import dev.definex.finalproject.dto.TaskDto;
import dev.definex.finalproject.entity.Project;
import dev.definex.finalproject.entity.Task;
import dev.definex.finalproject.entity.User;
import dev.definex.finalproject.enums.State;
import dev.definex.finalproject.enums.UserRole;
import dev.definex.finalproject.exception.*;
import dev.definex.finalproject.mapper.ProjectMapper;
import dev.definex.finalproject.mapper.TaskMapper;
import dev.definex.finalproject.mapper.UserMapper;
import dev.definex.finalproject.repository.ProjectRepository;
import dev.definex.finalproject.repository.TaskRepository;
import dev.definex.finalproject.repository.UserRepository;
import dev.definex.finalproject.service.TaskService;
import dev.definex.finalproject.tool.TaskStateValidator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskStateValidator taskStateValidator;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper,
                           UserMapper userMapper, ProjectRepository projectRepository
    , UserRepository userRepository,TaskStateValidator taskStateValidator) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskStateValidator = taskStateValidator;
    }


    @Override
    public TaskDto save(TaskDto taskDto) {
        Task savedTask = taskRepository.save(taskMapper.toTask(taskDto));
        return taskMapper.toTaskDto(savedTask);
    }

    @Override
    public List<TaskDto> findAll() {
        List<TaskDto> taskDtoList;
        List<Task> taskList = taskRepository.findAll();
        taskDtoList = taskList.stream().map((task -> taskMapper.toTaskDto(task))).toList();
        return taskDtoList;
    }

    @Override
    public List<TaskDto> findNotDeleted() {
        List<TaskDto> taskDtoList;
        List<Task> taskList = taskRepository.findByIsDeletedFalse();
        taskDtoList = taskList.stream().map((task -> taskMapper.toTaskDto(task))).toList();
        return taskDtoList;
    }

    @Override
    public TaskDto update(TaskDto taskDto) {
        Task task = taskRepository.findById(taskDto.getId()).orElseThrow(TaskNotFoundException::new);

        State currentState = task.getState();
        State newState = taskDto.getState();

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(UserNotFoundException::new);


        if (currentUser.getUserRole() == UserRole.Team_Member) {

            if (!task.getTitle().equals(taskDto.getTitle())) {
                throw new UnauthorizedActionException();
            }

            if (!task.getUserStoryDescription().equals(taskDto.getUserStoryDescription())) {
                throw new UnauthorizedActionException();
            }

            if (!task.getPriority().equals(taskDto.getPriority())) {
                throw new UnauthorizedActionException();
            }


            task.setState(newState);
        } else {

            task.setTitle(taskDto.getTitle());
            task.setUserStoryDescription(taskDto.getUserStoryDescription());
            task.setPriority(taskDto.getPriority());
            task.setAcceptanceCriteria(taskDto.getAcceptanceCriteria());
            task.setUserList(taskDto.getUserList().stream().map(userMapper::toUser).collect(Collectors.toList()));
            task.setProject(projectRepository.findById(taskDto.getProjectId()).orElseThrow(ProjectNotFoundException::new));
            task.setState(newState);
        }


        taskStateValidator.validateStateTransition(currentState, newState, taskDto.getReason());
        task.setReason((newState == State.Cancelled || newState == State.Blocked) ? taskDto.getReason() : null);

        task.setFiles(taskDto.getFiles());
        task.setComments(taskDto.getComments());

        taskRepository.save(task);
        return taskMapper.toTaskDto(task);
    }

    @Override
    public void delete(UUID uuid) throws TaskNotFoundException{
       if (taskRepository.existsById(uuid)){
           taskRepository.softDeleteById(uuid);
       }
       else
           throw new TaskNotFoundException();


    }

    @Override
    public TaskDto findById(UUID uuid) throws TaskNotFoundException {
        return taskMapper.toTaskDto(taskRepository.findById(uuid).orElseThrow(TaskNotFoundException::new));
    }
}
