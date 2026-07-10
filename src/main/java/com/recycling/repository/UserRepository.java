package com.recycling.repository;

import com.recycling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByIsActive(boolean isActive);
    List<User> findByRole(User.UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.isLocked = false AND u.isActive = true")
    List<User> findAllActiveAndNotLocked();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:search% OR u.email LIKE %:search%")
    List<User> searchUsers(@Param("search") String search);
}
