package com.yenyasoft.admin_user_management.enums.converter;

import com.yenyasoft.admin_user_management.enums.AccountStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Author: Subash
 * Created: 10/12/2026
 * File: AccounStatusConverter.java
 */
@Converter
public class AccountStatusConverter implements AttributeConverter<AccountStatus, String> {
    @Override
    public String convertToDatabaseColumn(AccountStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return switch (attribute){
            case PENDING -> "PENDING";
            case APPROVED -> "APPROVED";
            case REJECTED -> "REJECTED";
            case DISABLED-> "DISABLED";
        };
    }

    @Override
    public AccountStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return switch (dbData){
            case "PENDING" -> AccountStatus.PENDING;
            case "APPROVED" -> AccountStatus.APPROVED;
            case "REJECTED" -> AccountStatus.REJECTED;
            case "DISABLED" -> AccountStatus.DISABLED;
            default -> throw new IllegalArgumentException("No enum constant " + AccountStatus.class.getName());
        };

    }
}
