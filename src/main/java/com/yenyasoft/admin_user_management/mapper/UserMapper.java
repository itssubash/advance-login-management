package com.yenyasoft.admin_user_management.mapper;

import com.yenyasoft.admin_user_management.dtos.UserSearchRequest;
import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.enums.RoleType;
import com.yenyasoft.admin_user_management.models.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserMapper.java
 */
@Mapper
public interface UserMapper {

    User findById(@Param("userId") Integer userId);

    User findByUsername(@Param("username") String username);

    User findByEmail(@Param("email") String email);

    boolean existsByUsername(@Param("username") String username);

    boolean existsByEmail(@Param("email") String email);

    List<User> findAllUsers();

    List<User> findAllByRole(@Param("role") RoleType role);

    List<User> findAllByStatus(@Param("status") AccountStatus status);

    List<User> searchUsers(UserSearchRequest request);

    List<User> searchUsersPaged(UserSearchRequest request);

    long countSearchUsers(UserSearchRequest request);

    long countAllUsers();

    long countByStatus(@Param("status") AccountStatus status);
}
