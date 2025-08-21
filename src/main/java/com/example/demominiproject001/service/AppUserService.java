package com.example.demominiproject001.service;

import com.example.demominiproject001.model.request.UserUpdateRequest;
import com.example.demominiproject001.model.response.UserDTO;

public interface AppUserService {

    UserDTO getCurrentUser();

    UserDTO getUserByEmail(String email);

    UserDTO updateCurrentUser(UserUpdateRequest request);
}
