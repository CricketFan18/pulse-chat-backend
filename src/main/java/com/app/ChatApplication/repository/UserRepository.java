package com.app.ChatApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.ChatApplication.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
