package com.yenyasoft.admin_user_management.service.impl;

import com.yenyasoft.admin_user_management.converter.UserConverter;
import com.yenyasoft.admin_user_management.dtos.*;
import com.yenyasoft.admin_user_management.enums.AccountStatus;
import com.yenyasoft.admin_user_management.enums.UserAction;
import com.yenyasoft.admin_user_management.mapper.LoginHistoryMapper;
import com.yenyasoft.admin_user_management.mapper.UserMapper;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import com.yenyasoft.admin_user_management.models.User;
import com.yenyasoft.admin_user_management.repository.UserRepository;
import com.yenyasoft.admin_user_management.service.AdminService;
import com.yenyasoft.admin_user_management.service.UserApprovalHistoryService;
import com.yenyasoft.admin_user_management.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Author: Subash
 * Created: 10/13/2026
 * File: AdminServiceImpl.java
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoginHistoryMapper loginHistoryMapper;
    private final UserApprovalHistoryService userApprovalHistoryService;


    @Override
    public Page<UserDto> getAllUsers(UserSearchRequest request) {
        Pageable pageable = PagingUtils.createPageable(
                request.getPage(), request.getSize(), Sort.Direction.ASC, "userId");
        request.prepare(pageable);

        List<UserDto> users = userMapper
                .searchUsersPaged(request)
                .stream()
                .map(UserConverter::toUserDto)
                .toList();

        long totalElements = userMapper.countSearchUsers(request);
        return PagingUtils.toPage(users, totalElements, pageable);
    }

    @Override
    public UserDetailsResponse getUserDetails(Integer userId) {
        User user = findUser(userId);
        LoginHistory lastLogin = loginHistoryMapper.findLatestByUserId(userId);
        long totalLogins = loginHistoryMapper.countByUserId(userId);
        return new UserDetailsResponse(
                UserConverter.toUserDto(user),
                lastLogin != null ? lastLogin.getLoggedInAt() : null,
                totalLogins
        );
    }

    @Override
    @Transactional
    public UserDto approveAccount(Integer userId, String performedBy) {
        User pendingUser = findUser(userId);
        if (pendingUser.getStatus() != AccountStatus.PENDING) {
            throw new IllegalStateException("Only pending users can be approved");
        }
        pendingUser.setStatus(AccountStatus.APPROVED);
        pendingUser.setApprovedAt(LocalDateTime.now());
        pendingUser.setUpdatedAt(LocalDateTime.now());
        pendingUser.setRejectionReason(null);
        User approvedUser = userRepository.save(pendingUser);
        userApprovalHistoryService.record(userId, UserAction.APPROVED, null, performedBy);
        return UserConverter.toUserDto(approvedUser);
    }

    @Override
    @Transactional
    public UserDto rejectAccount(Integer userId, String reason, String performedBy) {
        User user = findUser(userId);
        if (user.getStatus() != AccountStatus.PENDING) {
            throw new IllegalStateException("Only pending users can be rejected");
        }
        user.setStatus(AccountStatus.REJECTED);
        user.setRejectionReason(reason);
        user.setUpdatedAt(LocalDateTime.now());
        User rejectedUser = userRepository.save(user);
        userApprovalHistoryService.record(userId, UserAction.REJECTED, reason, performedBy);

        return UserConverter.toUserDto(rejectedUser);
    }

    @Override
    @Transactional
    public UserDto disableAccount(Integer userId, String performedBy) {
        User user = findUser(userId);
        if (user.getStatus() != AccountStatus.APPROVED) {
            throw new IllegalStateException("Only approved users can be disabled");
        }
        user.setStatus(AccountStatus.DISABLED);
        user.setUpdatedAt(LocalDateTime.now());
        User disabledUser = userRepository.save(user);
        userApprovalHistoryService.record(userId, UserAction.DISABLED, null, performedBy);
        return UserConverter.toUserDto(disabledUser);
    }

    @Override
    @Transactional
    public UserDto enableAccount(Integer userId, String performedBy) {
        User user = findUser(userId);
        if (user.getStatus() != AccountStatus.DISABLED) {
            throw new IllegalStateException("Only disabled users can be enabled");
        }
        user.setStatus(AccountStatus.APPROVED);
        user.setUpdatedAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        User enabledUser = userRepository.save(user);
        userApprovalHistoryService.record(userId, UserAction.ENABLED, null, performedBy);
        return UserConverter.toUserDto(enabledUser);
    }

    @Override
    public DashboardResponse getDashboard() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX);
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().atTime(LocalTime.MAX);

        return new DashboardResponse(
                userMapper.countAllUsers(),
                userMapper.countByStatus(AccountStatus.PENDING),
                userMapper.countByStatus(AccountStatus.APPROVED),
                userMapper.countByStatus(AccountStatus.REJECTED),
                userMapper.countByStatus(AccountStatus.DISABLED),
                loginHistoryMapper.countByDateRange(startOfToday, endOfToday),
                loginHistoryMapper.countByDateRange(startOfMonth, endOfMonth)
        );
    }

    @Override
    public List<UserDto> getAllApprovedUsers(AccountStatus status) {
        return userMapper
                .findAllByStatus(AccountStatus.APPROVED)
                .stream()
                .map(UserConverter::toUserDto)
                .toList();
    }
    @Override
    public void reactivateAccount(ReApproveRequest request) {
        String email = request.getEmail();
        User user = userMapper.findByEmail(email);
        if (user.getStatus() == AccountStatus.DISABLED){
            user.setStatus(AccountStatus.APPROVED);
            user.setApprovedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        }
    }


    private User findUser(Integer userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }
}
