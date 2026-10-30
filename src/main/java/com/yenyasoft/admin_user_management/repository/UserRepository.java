package com.yenyasoft.admin_user_management.repository;

import com.yenyasoft.admin_user_management.models.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: UserRepository.java
 **/
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select user from User user where user.username = :username")
    Optional<User> findByUsernameForUpdate(@Param("username") String username);

    boolean existsByEmail(String email);
}
