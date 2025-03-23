package dev.definex.finalproject.dto.request;

import dev.definex.finalproject.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private UserRole userRole;
}