package com.yenyasoft.admin_user_management.repository;

import com.yenyasoft.admin_user_management.models.UserApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserApprovalHistoryRepository.java
 */
public interface UserApprovalHistoryRepository extends JpaRepository<UserApprovalHistory, Integer> {
}
