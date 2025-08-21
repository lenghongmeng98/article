package com.example.demominiproject001.repository;

import com.example.demominiproject001.model.entity.AppUser;
import com.example.demominiproject001.model.response.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> getUserByEmail(String username);

    Optional<AppUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
