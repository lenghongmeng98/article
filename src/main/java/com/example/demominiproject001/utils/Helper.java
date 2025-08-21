package com.example.demominiproject001.utils;

import com.example.demominiproject001.exception.ResourceNotFoundException;
import com.example.demominiproject001.model.entity.AppUser;
import com.example.demominiproject001.model.response.UserDTO;
import com.example.demominiproject001.repository.AppUserRepository;
import com.example.demominiproject001.security.jwt.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@RequiredArgsConstructor
public class Helper {

    private final AppUserRepository appUserRepository;

    public UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        AppUser appUser = appUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );
        return appUser.convertToUserDTO();
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userDetails.getUsername();
    }

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal= (UserPrincipal) auth.getPrincipal();
        return userPrincipal.getUserId();
    }
}
