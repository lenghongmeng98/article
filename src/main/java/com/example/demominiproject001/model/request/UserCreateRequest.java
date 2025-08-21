package com.example.demominiproject001.model.request;

import com.example.demominiproject001.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Pattern(
            regexp = "^(?:0\\d{9,14}|\\+885\\d{6,11})$",
            message = "Invalid phone number format. Must start with 0 or +885 and be 10-15 characters long"
    )
    private String phoneNumber;

    private String address;

    @NotNull(message = "Role is required and must be AUTHOR or READER")
    private Role role;
}
