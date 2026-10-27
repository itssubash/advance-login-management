package com.yenyasoft.admin_user_management.enums.converter;

import com.yenyasoft.admin_user_management.enums.UserAction;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Author: Subash
 * Created: 10/21/2026
 * File: UserActionConverter.java
 */
@Converter
public class UserActionConverter implements AttributeConverter<UserAction, String> {
    @Override
    public String convertToDatabaseColumn(UserAction attribute) {
        if (attribute == null) {
            return null;
        }
        return switch (attribute) {
            case APPROVED -> "APPROVED";
            case REJECTED -> "REJECTED";
            case DISABLED -> "DISABLED";
            case ENABLED -> "ENABLED";
        };
    }

    @Override
    public UserAction convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return switch (dbData) {
            case "APPROVED" -> UserAction.APPROVED;
            case "REJECTED" -> UserAction.REJECTED;
            case "DISABLED" -> UserAction.DISABLED;
            case "ENABLED" -> UserAction.ENABLED;
            default -> throw new IllegalArgumentException("No enum constant " + UserAction.class.getName());
        };
    }
}
