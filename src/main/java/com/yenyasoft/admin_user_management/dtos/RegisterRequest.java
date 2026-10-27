package com.yenyasoft.admin_user_management.dtos;

import com.yenyasoft.admin_user_management.validation.PasswordMatches;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: RegisterRequest.java
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50,
            message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72,
            message = "Password must be between 8 and 72 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Size(max = 100, message = "Address cannot exceed 100 characters")
    private String address;

    @Pattern(
            regexp = "^[0-9+() -]{7,30}$",
            message = "Please provide a valid phone number"
    )
    private String phoneNumber;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
}
