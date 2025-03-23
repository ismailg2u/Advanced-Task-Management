package dev.definex.finalproject.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.definex.finalproject.entity.Project;
import dev.definex.finalproject.entity.User;
import dev.definex.finalproject.enums.Priority;
import dev.definex.finalproject.enums.State;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class TaskDto {
    private UUID id;
    private Priority priority;
    private State state;
    private String title;
    private String userStoryDescription;
    private String acceptanceCriteria;
    private List<UserDto> userList;
    private List<String> comments;
    private List<String> files;
    private UUID projectId;
    private String reason;



}
