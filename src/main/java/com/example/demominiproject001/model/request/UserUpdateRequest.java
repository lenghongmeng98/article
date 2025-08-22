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
public class UserUpdateRequest {

    @Pattern(
            regexp = "^(?:0\\d{8,9}|\\+855\\d{8,9})$",
            message = "Invalid phone number format. Must start with 0 or +855 and follow by 8-9 characters long"
    )
    private String phoneNumber;

    @Schema(defaultValue = "Phnom Penh")
    private String address;
}

