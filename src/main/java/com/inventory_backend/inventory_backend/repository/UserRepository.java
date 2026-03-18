package com.inventory_backend.inventory_backend.repository;

import com.inventory_backend.inventory_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    // 🔍 Find user by email (login)

    // ✅ Check email already exists (signup)
    boolean existsByEmail(String email);
}
