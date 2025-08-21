package com.example.demominiproject001.service;

import com.example.demominiproject001.model.request.LoginRequest;
import com.example.demominiproject001.model.request.UserCreateRequest;
import com.example.demominiproject001.model.response.LoginResponse;
import com.example.demominiproject001.model.response.UserDTO;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    UserDTO register(UserCreateRequest request);
    LoginResponse refreshToken(String refreshToken);
}
