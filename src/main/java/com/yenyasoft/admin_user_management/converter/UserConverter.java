package com.yenyasoft.admin_user_management.converter;

import com.yenyasoft.admin_user_management.dtos.UserDto;
import com.yenyasoft.admin_user_management.dtos.UserProfileResponse;
import com.yenyasoft.admin_user_management.models.User;

import java.time.LocalDateTime;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: UserConverter.java
 */
public class UserConverter {
    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setAddress(user.getAddress());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setDob(user.getDob());
        userDto.setRejectionReason(user.getRejectionReason());
        userDto.setStatus(user.getStatus());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        userDto.setApprovedAt(user.getApprovedAt());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setDob(userDto.getDob());
        user.setStatus(userDto.getStatus());
        user.setRejectionReason(userDto.getRejectionReason());
        user.setRole(userDto.getRole());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setUpdatedAt(userDto.getUpdatedAt());
        user.setApprovedAt(userDto.getApprovedAt());
        return user;
    }

    public static UserProfileResponse toUserProfile(User user, LocalDateTime lastLogin, long totalLogins) {
        if (user == null) {
            return null;
        }
        UserProfileResponse profile = new UserProfileResponse();
        profile.setUserId(user.getUserId());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());
        profile.setAddress(user.getAddress());
        profile.setPhoneNumber(user.getPhoneNumber());
        profile.setDob(user.getDob());
        profile.setRole(user.getRole());
        profile.setStatus(user.getStatus());
        profile.setRejectionReason(user.getRejectionReason());
        profile.setCreatedAt(user.getCreatedAt());
        profile.setUpdatedAt(user.getUpdatedAt());
        profile.setApprovedAt(user.getApprovedAt());
        profile.setLastLogin(lastLogin);
        profile.setTotalLogins(totalLogins);
        return profile;
    }
}
