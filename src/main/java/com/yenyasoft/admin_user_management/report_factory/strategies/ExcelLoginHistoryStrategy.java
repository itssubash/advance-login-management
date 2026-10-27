package com.yenyasoft.admin_user_management.report_factory.strategies;

import com.yenyasoft.admin_user_management.enums.FileType;
import com.yenyasoft.admin_user_management.mapper.LoginHistoryMapper;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import com.yenyasoft.admin_user_management.report_factory.LoginHistoryExportStrategy;
import com.yenyasoft.admin_user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: ExcelLoginHistoryStrategy.java
 */
@Service
@RequiredArgsConstructor
public class ExcelLoginHistoryStrategy implements LoginHistoryExportStrategy {
    private final UserRepository userRepository;
    private final LoginHistoryMapper loginHistoryMapper;

    @Override
    public FileType getFormat() {
        return FileType.EXCEL;
    }

    @Override
    public byte[] export(Integer userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        List<LoginHistory> histories = loginHistoryMapper.findByUserId(userId);

        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Login History");
            String[] headers = {
                    "History ID", "User ID", "Username", "Successful",
                    "Login Status", "IP Address", "Browser",
                    "Operating System", "Device", "User Agent",
                    "Failure Reason", "Logged In At"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setWrapText(true);

            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNumber = 1;
            for (LoginHistory history : histories) {
                Row row = sheet.createRow(rowNumber++);

                row.createCell(0).setCellValue(
                        history.getHId() == null ? 0 : history.getHId());
                row.createCell(1).setCellValue(userId);
                row.createCell(2).setCellValue(safe(history.getUsername()));
                row.createCell(3).setCellValue(history.isSuccessful());
                row.createCell(4).setCellValue(
                        history.getLoginStatus() == null
                                ? "" : history.getLoginStatus().name());
                row.createCell(5).setCellValue(safe(history.getIpAddress()));
                row.createCell(6).setCellValue(safe(history.getBrowser()));
                row.createCell(7).setCellValue(
                        safe(history.getOperatingSystem()));
                row.createCell(8).setCellValue(safe(history.getDevice()));
                row.createCell(9).setCellValue(safe(history.getUserAgent()));
                row.createCell(10).setCellValue(
                        safe(history.getFailureReason()));
                row.createCell(11).setCellValue(
                        history.getLoggedInAt() == null
                                ? "" : history.getLoggedInAt().toString());
            }

            sheet.createFreezePane(0, 1);

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(output);
            return output.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Failed to generate Excel report", e);
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public String getFileExtension() {
        return ".xlsx";
    }
}
