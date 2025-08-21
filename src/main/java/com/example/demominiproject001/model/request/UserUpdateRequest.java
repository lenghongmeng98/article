package com.example.demominiproject001.model.request;

import com.example.demominiproject001.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @Pattern(
            regexp = "^(?:0\\d{9,14}|\\+885\\d{6,11})$",
            message = "Invalid phone number format. Must start with 0 or +885 and be 10-15 characters long"
    )
    private String phoneNumber;

    private String address;
}

