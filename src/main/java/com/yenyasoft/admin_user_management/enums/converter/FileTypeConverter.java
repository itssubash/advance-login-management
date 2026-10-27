package com.yenyasoft.admin_user_management.enums.converter;

import com.yenyasoft.admin_user_management.enums.FileType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: FileTypeConverter.java
 */
@Converter(autoApply = true)
public class FileTypeConverter implements AttributeConverter<FileType, String> {
    @Override
    public String convertToDatabaseColumn(FileType attribute) {
        if(attribute == null) return null;
        return switch (attribute){
             case PDF -> "PDF";
             case WORD ->  "WORD";
             case EXCEL -> "EXCEL";
         };
    }

    @Override
    public FileType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        return switch (dbData){
            case "PDF" -> FileType.PDF;
            case "WORD" -> FileType.WORD;
            case "EXCEL" -> FileType.EXCEL;
            default -> throw new IllegalArgumentException("Unknown file type" + dbData);
        };

    }
}
