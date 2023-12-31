package com.shopme.admin.user.export;

import com.shopme.common.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter(){
        workbook = new XSSFWorkbook();
    }
    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream",".xlsx");

        writeHeaderLine();
        writeDataLines(userList);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
    private void writeHeaderLine(){
        sheet = workbook.createSheet("Users");
        XSSFRow row = sheet.createRow(0);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "User Id", cellStyle);
        createCell(row, 1, "Email", cellStyle);
        createCell(row, 2, "First Name", cellStyle);
        createCell(row, 3, "Last Name", cellStyle);
        createCell(row, 4, "Roles", cellStyle);
        createCell(row, 5, "Enabled", cellStyle);
    }
    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle){
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        }else if(value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        }else{
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(cellStyle);
    }

    private void writeDataLines(List<User> userList) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for(User user : userList){
            XSSFRow row = sheet.createRow(rowIndex++);
            int colIndex = 0;

            createCell(row, colIndex++, user.getId(), cellStyle);
            createCell(row, colIndex++, user.getEmail(), cellStyle);
            createCell(row, colIndex++, user.getFirstName(), cellStyle);
            createCell(row, colIndex++, user.getLastName(), cellStyle);
            createCell(row, colIndex++, user.getRoles().toString(), cellStyle);
            createCell(row, colIndex++, user.isEnabled(), cellStyle);
        }
    }
}
