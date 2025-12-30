package com.gestion.stock.repository;

import com.gestion.stock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 * Contains optimized queries to avoid N+1 query problem
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    
    /**
     * Find user by username with role and user permissions loaded
     * Role's default permissions are fetched separately to avoid MultipleBagFetchException
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.role r " +
           "LEFT JOIN FETCH u.userPermissions up " +
           "LEFT JOIN FETCH up.permission " +
           "WHERE u.username = :username")
    Optional<User> findUserByUsername(@Param("username") String username);

    /**
     * Check if username exists (lightweight query for registration)
     */
    boolean existsByUsername(String username);
}
