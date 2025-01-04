package com.example.connect.domain.user.repository;

import com.example.connect.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    Optional<User> findByEmail(String email);
}
