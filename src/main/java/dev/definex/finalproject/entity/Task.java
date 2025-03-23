package dev.definex.finalproject.entity;

import dev.definex.finalproject.enums.Priority;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Priority priority;
    private State state;
    private String title;
    private String userStoryDescription;
    private String acceptanceCriteria;
    @ManyToMany
    @JoinTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> userList;
    private List<String> comments;
    private List<String> files;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    @Builder.Default
    private boolean isDeleted = false;
    private String reason;




}
