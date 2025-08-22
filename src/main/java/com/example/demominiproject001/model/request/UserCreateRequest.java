package com.example.demominiproject001.model.request;

import com.example.demominiproject001.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @Schema(defaultValue = "example")
    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;

    @Schema(defaultValue = "example@gmail.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(defaultValue = "Passw@rd123")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "Password must be at least 8 characters and include at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;


    @Pattern(
            regexp = "^(?:0\\d{8,9}|\\+885\\d{8,9})$",
            message = "Invalid phone number format. Must start with 0 or +885 and follow by 8-9 characters long"
    )
    private String phoneNumber;

    @Schema(defaultValue = "Phnom Penh")
    private String address;

    @NotNull(message = "Role is required and must be AUTHOR or READER")
    private Role role;
}
