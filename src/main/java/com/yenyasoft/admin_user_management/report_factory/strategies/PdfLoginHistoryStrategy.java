package com.yenyasoft.admin_user_management.report_factory.strategies;

import com.yenyasoft.admin_user_management.enums.FileType;
import com.yenyasoft.admin_user_management.mapper.LoginHistoryMapper;
import com.yenyasoft.admin_user_management.models.LoginHistory;
import com.yenyasoft.admin_user_management.report_factory.LoginHistoryExportStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Author: Subash
 * Created: 10/29/2026
 * File: PdfLoginHistoryStrategy.java
 */
@Service
@RequiredArgsConstructor
public class PdfLoginHistoryStrategy implements LoginHistoryExportStrategy {

    private static final PDRectangle PAGE_SIZE = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
    private static final float MARGIN = 30f;
    private static final float ROW_HEIGHT = 20f;
    private static final float CELL_PADDING = 4f;
    private static final float FONT_SIZE = 9f;
    private static final float TITLE_SIZE = 16f;

    private static final String[] HEADERS =
            {"#", "Username", "Status", "IP Address", "Browser", "OS", "Device", "Logged In At"};

    private static final float[] COL_WIDTHS = {30, 110, 70, 100, 110, 110, 90, 162};

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private final PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    private final LoginHistoryMapper  loginHistoryMapper;

    @Override
    public FileType getFormat() {
        return FileType.PDF;
    }

    @Override
    public byte[] export(Integer userId) throws RuntimeException {
        List<LoginHistory> histories = loginHistoryMapper.findByUserId(userId);

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PAGE_SIZE);
            document.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(document, page);

            float y = PAGE_SIZE.getHeight() - MARGIN;

            // Title
            cs.beginText();
            cs.setFont(fontBold, TITLE_SIZE);
            cs.newLineAtOffset(MARGIN, y - TITLE_SIZE);
            cs.showText("Login History Report");
            cs.endText();
            y -= TITLE_SIZE + 25;

            y = drawHeaderRow(cs, y);

            if (histories.isEmpty()) {
                drawText(cs, fontRegular, "No login history found.", MARGIN + CELL_PADDING, y - ROW_HEIGHT + 6);
            }

            int index = 1;
            for (LoginHistory h : histories) {
                if (y - ROW_HEIGHT < MARGIN) {
                    cs.close();
                    page = new PDPage(PAGE_SIZE);
                    document.addPage(page);
                    cs = new PDPageContentStream(document, page);
                    y = PAGE_SIZE.getHeight() - MARGIN;
                    y = drawHeaderRow(cs, y);
                }

                String[] cells = {
                        String.valueOf(index++),
                        h.getUsername(),
                        h.getLoginStatus() != null ? h.getLoginStatus().name() : "",
                        h.getIpAddress(),
                        h.getBrowser(),
                        h.getOperatingSystem(),
                        h.getDevice(),
                        h.getLoggedInAt() != null ? h.getLoggedInAt().format(DATE_FMT) : ""
                };
                y = drawRow(cs, y, cells, fontRegular, null);
            }

            cs.close();
            document.save(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate login history PDF", e);
        }
    }

    @Override
    public String getContentType() {
        return "application/pdf";
    }

    @Override
    public String getFileExtension() {
        return ".pdf";
    }


    private float drawHeaderRow(PDPageContentStream cs, float y) throws IOException {
        return drawRow(cs, y, HEADERS, fontBold, new Color(220, 220, 220));
    }

    private float drawRow(PDPageContentStream cs, float y, String[] cells,
                          PDType1Font font, Color background) throws IOException {
        float x = MARGIN;
        float rowBottom = y - ROW_HEIGHT;

        for (int i = 0; i < cells.length; i++) {
            float w = COL_WIDTHS[i];

            if (background != null) {
                cs.setNonStrokingColor(background);
                cs.addRect(x, rowBottom, w, ROW_HEIGHT);
                cs.fill();
            }

            cs.setStrokingColor(Color.BLACK);
            cs.setLineWidth(0.5f);
            cs.addRect(x, rowBottom, w, ROW_HEIGHT);
            cs.stroke();

            cs.setNonStrokingColor(Color.BLACK);
            String text = fit(cells[i], font, w - 2 * CELL_PADDING);
            drawText(cs, font, text, x + CELL_PADDING, rowBottom + 6);

            x += w;
        }
        return rowBottom;
    }

    private void drawText(PDPageContentStream cs, PDType1Font font, String text,
                          float x, float y) throws IOException {
        cs.beginText();
        cs.setFont(font, FONT_SIZE);
        cs.newLineAtOffset(x, y);
        cs.showText(sanitize(text));
        cs.endText();
    }

    private String fit(String text, PDType1Font font, float maxWidth) throws IOException {
        String clean = sanitize(text);
        if (width(clean, font) <= maxWidth) {
            return clean;
        }
        String ellipsis = "...";
        while (!clean.isEmpty() && width(clean + ellipsis, font) > maxWidth) {
            clean = clean.substring(0, clean.length() - 1);
        }
        return clean + ellipsis;
    }

    private float width(String text, PDType1Font font) throws IOException {
        return font.getStringWidth(text) / 1000f * FONT_SIZE;
    }


    private String sanitize(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            sb.append((c >= 32 && c <= 126) ? c : ' ');
        }
        return sb.toString().trim();
    }
}