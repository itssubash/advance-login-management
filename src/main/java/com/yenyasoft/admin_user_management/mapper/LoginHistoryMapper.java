package com.yenyasoft.admin_user_management.mapper;

import com.yenyasoft.admin_user_management.dtos.LoginHistorySearchRequest;
import com.yenyasoft.admin_user_management.enums.LoginStatus;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: LoginHistoryMapper.java
 */
@Mapper
public interface LoginHistoryMapper {

    LoginHistory findById(@Param("hId") Integer hId);

    List<LoginHistory> findByUserId(@Param("userId") Integer userId);

    List<LoginHistory> findAllHistory();

    List<LoginHistory> findByUsername(@Param("username") String username);

    List<LoginHistory> findByLoginStatus(@Param("loginStatus") LoginStatus loginStatus);

    List<LoginHistory> findByDateRange(@Param("fromDate") LocalDateTime fromDate,
                                       @Param("toDate") LocalDateTime toDate);

    List<LoginHistory> searchHistory(LoginHistorySearchRequest request);

    List<LoginHistory> searchHistoryPaged(LoginHistorySearchRequest request);

    long countSearchHistory(LoginHistorySearchRequest request);

    LoginHistory findLatestByUserId(@Param("userId") Integer userId);

    long countByUserId(@Param("userId") Integer userId);

    long countByDateRange(@Param("fromDate") LocalDateTime fromDate,
                          @Param("toDate") LocalDateTime toDate);
}
