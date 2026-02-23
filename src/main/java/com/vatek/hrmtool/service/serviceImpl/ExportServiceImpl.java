package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.dto.TimesheetDto.DailyWorkingHoursDto;
import com.vatek.hrmtool.dto.TimesheetDto.EmployeeInProjectDto;
import com.vatek.hrmtool.dto.TimesheetDto.ReportDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExportServiceImpl {
    public ByteArrayInputStream exportToExcel(List<ReportDto> reports, YearMonth yearMonth) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            for (ReportDto report : reports) {
                createSheet(workbook, report, yearMonth);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportToExcel(List<ReportDto> reports) throws IOException {
        return exportToExcel(reports, YearMonth.now());
    }

    private void createSheet(Workbook workbook, ReportDto report, YearMonth yearMonth) {
        Sheet sheet = workbook.createSheet(report.getProjectName());
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        List<LocalDate> allDatesInMonth = new ArrayList<>();
        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            allDatesInMonth.add(date);
        }
        Row headerRow = sheet.createRow(0);
        createHeaderCell(workbook, headerRow, 0, "Full Name");
        createHeaderCell(workbook, headerRow, 1, "TWH");
        createHeaderCell(workbook, headerRow, 2, "TBH");
        createHeaderCell(workbook, headerRow, 3, "TOH");
        
        int colIndex = 4;
        for (LocalDate date : allDatesInMonth) {
            createHeaderCell(workbook, headerRow, colIndex++, formatDate(date));
        }
        int rowIndex = 1;
        for (EmployeeInProjectDto employee : report.getEmployees()) {
            Row row = sheet.createRow(rowIndex++);
            
            createDataCell(workbook, row, 0, employee.getName());
            createDataCell(workbook, row, 1, employee.getTotalNormalHours());
            createDataCell(workbook, row, 2, employee.getTotalBonusHours());
            createDataCell(workbook, row, 3, employee.getTotalOvertimeHours());
            
            Map<LocalDate, Double> dailyHours = employee.getDailyWorkingHourDtos().stream()
                .collect(Collectors.toMap(
                    DailyWorkingHoursDto::getDate,
                    DailyWorkingHoursDto::getHour
                ));

            colIndex = 4;
            for (LocalDate date : allDatesInMonth) {
                double hours = dailyHours.getOrDefault(date, 0.0);
                createDataCell(workbook, row, colIndex++, hours);
            }
        }
        for (int i = 0; i < 4 + allDatesInMonth.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createHeaderCell(Workbook workbook, Row row, int colIndex, String value) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(style);
    }

    private void createDataCell(Workbook workbook, Row row, int colIndex, Object value) {
        Cell cell = row.createCell(colIndex);
        
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(style);
    }

    private String formatDate(LocalDate date) {
        return date.getDayOfMonth() + "/" + date.getMonthValue();
    }
}
