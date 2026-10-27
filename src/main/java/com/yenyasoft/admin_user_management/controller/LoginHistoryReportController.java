package com.yenyasoft.admin_user_management.controller;
import com.yenyasoft.admin_user_management.enums.FileType;
import com.yenyasoft.admin_user_management.report_factory.LoginHistoryExportStrategy;
import com.yenyasoft.admin_user_management.report_factory.report_factory.LoginHistoryReportFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: LoginHistoryReportController.java
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login-history")
public class LoginHistoryReportController {
    private final LoginHistoryReportFactory reportFactory;

    @GetMapping("/report/{userId}")
    public ResponseEntity<byte[]> downloadLoginHistoryReport(
            @PathVariable Integer userId,
            @RequestParam FileType format) {
        LoginHistoryExportStrategy strategy = reportFactory.getStrategy(format);
        byte[] report = strategy.export(userId);

        String filename = "login-history-" + userId + strategy.getFileExtension();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        strategy.getContentType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\""
                )
                .body(report);
    }
}
