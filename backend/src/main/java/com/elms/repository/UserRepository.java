package com.elms.repository;

import com.elms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for User entity data access
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by unique email address
    Optional<User> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Find all direct reports for a given manager
    List<User> findByManagerId(Long managerId);
}
