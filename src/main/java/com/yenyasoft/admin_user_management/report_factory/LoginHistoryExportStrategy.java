package com.yenyasoft.admin_user_management.report_factory;

import com.yenyasoft.admin_user_management.enums.FileType;




/**
 * Author: Subash
 * Created: 10/27/2026
 * File: LoginHistoryExportStrategy.java
 */
public interface LoginHistoryExportStrategy {
    FileType getFormat();

    byte[] export(Integer userId) throws RuntimeException;

    String getContentType();

    String getFileExtension();

}
