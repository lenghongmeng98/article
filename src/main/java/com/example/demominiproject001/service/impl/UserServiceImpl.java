package com.example.demominiproject001.service.impl;

import com.example.demominiproject001.exception.ResourceNotFoundException;
import com.example.demominiproject001.model.entity.AppUser;
import com.example.demominiproject001.model.request.UserUpdateRequest;
import com.example.demominiproject001.model.response.UserDTO;
import com.example.demominiproject001.repository.AppUserRepository;
import com.example.demominiproject001.service.AppUserService;
import com.example.demominiproject001.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final Helper helper;

    @Override
    public UserDTO getCurrentUser() {

        AppUser user = appUserRepository.getUserByEmail(helper.getCurrentUserEmail()).orElseThrow(
                () -> new UsernameNotFoundException("User not found: " + helper.getCurrentUserEmail())
        );

        return user.convertToUserDTO();
    }

    @Override
    public UserDTO getUserByEmail(String email) {

        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email)
        );

        return user.convertToUserDTO();
    }

    @Transactional
    @Override
    public UserDTO updateCurrentUser(UserUpdateRequest request) {

        String email = helper.getCurrentUserEmail();

        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email)
        );

        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());

        AppUser updatedUser = appUserRepository.save(user);

        return updatedUser.convertToUserDTO();
    }
}
