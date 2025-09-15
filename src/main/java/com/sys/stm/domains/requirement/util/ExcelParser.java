package com.sys.stm.domains.requirement.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelParser {

    public List<Map<String, Object>> parse(MultipartFile file) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return data;
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                String header = cell.getStringCellValue().trim();
                if (!header.isEmpty()) {
                    headers.add(header);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, Object> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    String header = headers.get(j);
                    Object value = "";

                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:    value = cell.getStringCellValue(); break;
                            case NUMERIC:   value = DateUtil.isCellDateFormatted(cell) ? sdf.format(cell.getDateCellValue()) : getNumericValue(cell); break;
                            case BOOLEAN:   value = cell.getBooleanCellValue(); break;
                            case FORMULA:   value = getFormulaValue(cell); break;
                            case ERROR:     value = null; break;
                            default:        value = ""; break;
                        }
                    }
                    rowData.put(header, value);
                }

                if (!isRowEffectivelyEmpty(rowData)) {
                    data.add(rowData);
                }
            }
        }
        return data;
    }

    private Object getNumericValue(Cell cell) {
        double numericValue = cell.getNumericCellValue();
        if (Double.isNaN(numericValue) || Double.isInfinite(numericValue)) {
            return null;
        }
        return (numericValue == (long) numericValue) ? (long) numericValue : numericValue;
    }

    private Object getFormulaValue(Cell cell) {
        try {
            switch (cell.getCachedFormulaResultType()) {
                case NUMERIC:   return getNumericValue(cell); 
                case STRING:    return cell.getRichStringCellValue().getString();
                case ERROR:     return null;
                default:        return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private boolean isRowEffectivelyEmpty(Map<String, Object> rowData) {
        for (Map.Entry<String, Object> entry : rowData.entrySet()) {
            if (entry.getKey().equals("Req.ID")) {
                continue;
            }
            if (entry.getValue() != null && !entry.getValue().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
