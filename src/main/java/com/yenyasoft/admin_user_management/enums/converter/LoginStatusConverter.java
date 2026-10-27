package com.yenyasoft.admin_user_management.enums.converter;

import com.yenyasoft.admin_user_management.enums.LoginStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: LoginStatusConverter.java
 */
@Converter
public class LoginStatusConverter implements AttributeConverter<LoginStatus, String> {
    @Override
    public String convertToDatabaseColumn(LoginStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return switch (attribute) {
            case SUCCESS -> "SUCCESS";
            case FAILED -> "FAILED";
        };
    }

    @Override
    public LoginStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return switch (dbData) {
            case "SUCCESS" -> LoginStatus.SUCCESS;
            case "FAILED" -> LoginStatus.FAILED;
            default -> throw new IllegalArgumentException("No enum constant " + LoginStatus.class.getName());
        };
    }
}
