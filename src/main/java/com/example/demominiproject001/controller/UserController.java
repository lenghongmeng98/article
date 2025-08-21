package com.example.demominiproject001.controller;

import com.example.demominiproject001.model.request.UserUpdateRequest;
import com.example.demominiproject001.model.response.ApiResponse;
import com.example.demominiproject001.model.response.UserDTO;
import com.example.demominiproject001.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final AppUserService userService;

    @GetMapping
    @Operation(summary = "Get current user. can be used by all roles")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponse.success("Get current user successfully", userService.getCurrentUser()));
    }

    @PutMapping()
    @Operation(summary = "Update current user. can be used by all roles")
    public ResponseEntity<ApiResponse<UserDTO>> updateCurrentUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        UserDTO updatedUser = userService.updateCurrentUser(userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success("Updated current user successfully", updatedUser));
    }
}
