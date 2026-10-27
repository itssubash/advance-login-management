package com.yenyasoft.admin_user_management.enums.converter;

import com.yenyasoft.admin_user_management.enums.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: RoleTypeConverter.java
 */
@Converter
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {
    @Override
    public String convertToDatabaseColumn(RoleType attribute) {
        if (attribute == null) {
            return null;
        }
        return switch (attribute) {
            case ADMIN -> "ADMIN";
            case USER -> "USER";
        };
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {
        if (dbData == null) {return null;}

        return switch (dbData){
            case "ADMIN" -> RoleType.ADMIN;
            case "USER" -> RoleType.USER;
            default -> throw new IllegalArgumentException("Unknown RoleType: " + dbData);
        };

    }
}
