package com.example.demominiproject001.controller;

import com.example.demominiproject001.model.request.LoginRequest;
import com.example.demominiproject001.model.request.UserCreateRequest;
import com.example.demominiproject001.model.response.ApiResponse;
import com.example.demominiproject001.model.response.LoginResponse;
import com.example.demominiproject001.model.response.UserDTO;
import com.example.demominiproject001.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successfully", jwtResponse));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody UserCreateRequest request) {
        UserDTO userResponse = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User registered successfully", userResponse));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh new token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestParam String refreshToken) {
        LoginResponse loginResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", loginResponse));
    }
}
