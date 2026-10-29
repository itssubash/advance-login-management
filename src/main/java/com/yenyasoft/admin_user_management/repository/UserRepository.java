package com.yenyasoft.admin_user_management.repository;

import com.yenyasoft.admin_user_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
