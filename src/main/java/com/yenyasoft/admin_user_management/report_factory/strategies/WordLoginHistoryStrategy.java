
package com.yenyasoft.admin_user_management.report_factory.strategies;

import com.yenyasoft.admin_user_management.enums.FileType;
import com.yenyasoft.admin_user_management.mapper.LoginHistoryMapper;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import com.yenyasoft.admin_user_management.report_factory.LoginHistoryExportStrategy;
import com.yenyasoft.admin_user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Author: Subash
 * Created: 10/27/2026
 * File: WordLoginHistoryStrategy.java
 */
@Service
@RequiredArgsConstructor
public class WordLoginHistoryStrategy implements LoginHistoryExportStrategy {

    private final UserRepository userRepository;
    private final LoginHistoryMapper loginHistoryMapper;

    @Override
    public FileType getFormat() {
        return FileType.WORD;
    }

    @Override
    public byte[] export(Integer userId) throws RuntimeException {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
        }

        List<LoginHistory> histories = loginHistoryMapper.findByUserId(userId);

        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);

            XWPFRun titleRun = title.createRun();
            titleRun.setText("Login History Report");
            titleRun.setBold(true);
            titleRun.setFontSize(18);

            XWPFParagraph userParagraph = document.createParagraph();
            userParagraph.createRun().setText("User ID: " + userId);

            String[] headers = {
                    "History ID",
                    "Username",
                    "Successful",
                    "Status",
                    "IP Address",
                    "Browser",
                    "Operating System",
                    "Device",
                    "User Agent",
                    "Failure Reason",
                    "Logged In At"
            };

            XWPFTable table = document.createTable();
            XWPFTableRow headerRow = table.getRow(0);

            for (int i = 0; i < headers.length; i++) {
                XWPFTableCell cell = (i == 0)
                        ? headerRow.getCell(0)
                        : headerRow.addNewTableCell();

                cell.setText(headers[i]);

                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    for (XWPFRun run : paragraph.getRuns()) {
                        run.setBold(true);
                    }
                }
            }

            for (LoginHistory history : histories) {
                XWPFTableRow row = table.createRow();

                String[] values = {
                        history.getHId() == null
                                ? ""
                                : history.getHId().toString(),
                        safe(history.getUsername()),
                        Boolean.toString(history.isSuccessful()),
                        history.getLoginStatus() == null
                                ? ""
                                : history.getLoginStatus().name(),
                        safe(history.getIpAddress()),
                        safe(history.getBrowser()),
                        safe(history.getOperatingSystem()),
                        safe(history.getDevice()),
                        safe(history.getUserAgent()),
                        safe(history.getFailureReason()),
                        history.getLoggedInAt() == null
                                ? ""
                                : history.getLoggedInAt().toString()
                };

                for (int i = 0; i < values.length; i++) {
                    row.getCell(i).setText(values[i]);
                }
            }

            document.write(output);
            return output.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to generate Word report",
                    e
            );
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument."
                + "wordprocessingml.document";
    }

    @Override
    public String getFileExtension() {
        return ".docx";
    }
}
