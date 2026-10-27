package com.yenyasoft.admin_user_management.mapper;

import com.yenyasoft.admin_user_management.enums.UserAction;
import com.yenyasoft.admin_user_management.models.UserApprovalHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserApprovalHistoryMapper.java
 */
@Mapper
public interface UserApprovalHistoryMapper {

    UserApprovalHistory findById(@Param("approvalId") Integer approvalId);

    List<UserApprovalHistory> findByUserId(@Param("userId") Integer userId);

    List<UserApprovalHistory> findAllApprovalHistory();

    List<UserApprovalHistory> findByAction(@Param("action") UserAction action);

    long countByAction(@Param("action") UserAction action);
}
