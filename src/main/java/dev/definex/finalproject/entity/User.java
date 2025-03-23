package dev.definex.finalproject.entity;

import dev.definex.finalproject.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private UserRole userRole;
    @Builder.Default
    private boolean isDeleted = false;
    @ManyToMany(mappedBy = "userList")
    private List<Task> taskList;
    @ManyToMany(mappedBy = "userList")
    private List<Project> projectList;

}
