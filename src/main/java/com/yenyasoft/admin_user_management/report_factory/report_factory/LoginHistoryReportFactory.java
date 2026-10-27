package com.yenyasoft.admin_user_management.report_factory.report_factory;

import com.yenyasoft.admin_user_management.enums.FileType;
import com.yenyasoft.admin_user_management.report_factory.LoginHistoryExportStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: LoginHistoryReportFactory.java
 */
@Component
public class LoginHistoryReportFactory {
    private final Map<FileType, LoginHistoryExportStrategy> strategies = new EnumMap<>(FileType.class);

    public LoginHistoryReportFactory(List<LoginHistoryExportStrategy> exporters) {

        for (LoginHistoryExportStrategy exporter : exporters) {
            LoginHistoryExportStrategy previous = strategies.put(exporter.getFormat(), exporter);

            if (previous != null) {
                throw new IllegalStateException("Duplicate exporter for format: " + exporter.getFormat());
            }
        }
    }

    public LoginHistoryExportStrategy getStrategy(FileType format) {
        LoginHistoryExportStrategy strategy = strategies.get(format);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported report format: " + format);
        }
        return strategy;
    }
}
