package com.yenyasoft.admin_user_management.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UpdateProfileRequest.java
 */
@Data
public class UpdateProfileRequest {
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Size(max = 100, message = "Address cannot exceed 100 characters")
    private String address;

    @Pattern(
            regexp = "^[0-9+() -]{7,30}$",
            message = "Please provide a valid phone number"
    )
    private String phoneNumber;

    private LocalDate dob;
}
