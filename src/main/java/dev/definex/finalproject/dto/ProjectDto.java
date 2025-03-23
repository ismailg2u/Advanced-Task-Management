package dev.definex.finalproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.definex.finalproject.entity.Department;
import dev.definex.finalproject.entity.Task;
import dev.definex.finalproject.entity.User;
import dev.definex.finalproject.enums.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private UUID id;
    private String title;
    private String description;
    private State state;
    private List<UserDto> userList;
    private UUID departmentId;
    private List<TaskDto> tasks;
    private String departmentName;


}
