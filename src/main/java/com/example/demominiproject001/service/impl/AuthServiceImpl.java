package com.example.demominiproject001.service.impl;

import com.example.demominiproject001.exception.AlreadyExistsException;
import com.example.demominiproject001.exception.ResourceNotFoundException;
import com.example.demominiproject001.model.entity.AppUser;
import com.example.demominiproject001.model.request.LoginRequest;
import com.example.demominiproject001.model.request.UserCreateRequest;
import com.example.demominiproject001.model.response.LoginResponse;
import com.example.demominiproject001.model.response.UserDTO;
import com.example.demominiproject001.repository.AppUserRepository;
import com.example.demominiproject001.security.jwt.JwtUtil;
import com.example.demominiproject001.service.UserDetailsServiceImpl;
import com.example.demominiproject001.service.AuthService;
import com.example.demominiproject001.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AppUserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        appUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public UserDTO register(UserCreateRequest request) {

        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();

        AppUser savedUser = appUserRepository.save(user);
        return savedUser.convertToUserDTO();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {

        String email = jwtUtil.extractEmail(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if(jwtUtil.validateToken(refreshToken, userDetails) && jwtUtil.isRefreshToken(refreshToken)) {

            String newAccessToken = jwtUtil.generateToken(userDetails);
            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

            appUserRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            return LoginResponse.builder()
                    .token(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        else {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}
