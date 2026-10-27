package com.yenyasoft.admin_user_management.repository;

import com.yenyasoft.admin_user_management.models.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: LoginHistoryRepository.java
*/
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
}
