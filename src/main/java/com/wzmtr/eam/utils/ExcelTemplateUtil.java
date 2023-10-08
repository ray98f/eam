package com.wzmtr.eam.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.wzmtr.eam.entity.DynamicSource;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 模板导出工具类，占位符使用形如{{time}}、{{x.time}}的格式
 * Author: Li.Wang
 * Date: 2023/9/25 11:30
 */
public class ExcelTemplateUtil {

    public static Workbook buildByTemplate(InputStream inputStream, Map<String, String> staticSource, List<DynamicSource> dynamicSourceList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        // 遍历所有的sheet
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheetAt = workbook.getSheetAt(i);
            handleSheet(sheetAt, staticSource, dynamicSourceList);
            // _handleFormula(i, sheetAt);
            sheetAt.setForceFormulaRecalculation(true);
        }
        // 使用evaluateFormulaCell对函数单元格进行强行更新计算
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        return workbook;
    }

    // private static void _handleFormula(int i, XSSFSheet sheetAt) {
    //     // 为第一个sheet设置公式
    //     if (i == 0) {
    //         sheetAt.getRow(2).getCell(2).setCellFormula("C10-D10");
    //     }
    // }

    public static void save(Workbook workbook, String excelFilePath) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(excelFilePath);
        save(workbook, outputStream);
    }

    public static void save(Workbook workbook, String excelName, HttpServletResponse response) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + new String(excelName.trim().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8) + ".xlsx";
        response.setHeader("Content-Disposition", "attachmentuan;filename=" + fileName);
        response.setContentType("application/x-msdownload;charset=utf-8");
        OutputStream outputStream = response.getOutputStream();// 不同类型的文件对应不同的MIME类型
        save(workbook, outputStream);
    }

    public static void save(Workbook workbook, OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        workbook.close();
    }

    private static void handleSheet(XSSFSheet sheet, Map<String, String> staticSource, List<DynamicSource> dynamicSourceList) {
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            DynamicSource dynamicSource = parseDynamicRow(row, dynamicSourceList);
            if (dynamicSource != null) {
                i = handleDynamicRows(dynamicSource, sheet, i);
            } else {
                replaceRowValue(row, staticSource, null);
            }
        }
    }

    private static DynamicSource parseDynamicRow(XSSFRow row, List<DynamicSource> dynamicSourceList) {
        if (CollectionUtil.isEmpty(dynamicSourceList)) {
            return null;
        }
        if (row == null) {
            return null;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            XSSFCell cell = row.getCell(i);
            if (cell != null) {
                if (cell.getCellType() == CellType.FORMULA) {
                    continue;
                }
                cell.setCellType(CellType.STRING);
                String value = cell.getStringCellValue();
                if (value != null) {
                    for (DynamicSource current : dynamicSourceList) {
                        if (value.startsWith("{{" + current.getId() + ".")) {
                            return current;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static int handleDynamicRows(DynamicSource dynamicSource, XSSFSheet sheet, int rowIndex) {
        if (dynamicSource == null) {
            return rowIndex;
        }
        int rows = dynamicSource.getDataList().size();
        // 因为模板行本身占1行，所以-1
        int copyRows = rows - 1;
        if (copyRows > 0) {
            // shiftRows: 从startRow到最后一行，全部向下移copyRows行
            sheet.shiftRows(rowIndex, sheet.getLastRowNum(), copyRows, true, false);
            // 拷贝策略
            CellCopyPolicy cellCopyPolicy = new CellCopyPolicy();
            cellCopyPolicy.setCopyCellValue(true);
            cellCopyPolicy.setCopyCellFormula(true);
            cellCopyPolicy.setCopyCellStyle(true);
            // 这里模板row已经变成了startRow + copyRows,
            int templateRow = rowIndex + copyRows;
            // 因为下移了，所以要把模板row拷贝到所有空行
            for (int i = 0; i < copyRows; i++) {
                sheet.copyRows(templateRow, templateRow, rowIndex + i, cellCopyPolicy);
            }
        }
        // 替换动态行的值
        for (int j = rowIndex; j < rowIndex + rows; j++) {
            replaceRowValue(sheet.getRow(j), dynamicSource.getDataList().get(j - rowIndex), dynamicSource.getId());
        }
        return rowIndex + copyRows;
    }

    private static void replaceRowValue(XSSFRow row, Map<String, String> map, String prefixKey) {
        if (CollectionUtil.isEmpty(map)) {
            return;
        }
        if (row == null) {
            return;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            XSSFCell cell = row.getCell(i);
            replaceCellValue(cell, map, prefixKey);
        }
    }

    private static void replaceCellValue(XSSFCell cell, Map<String, String> map, String prefixKey) {
        if (cell == null) {
            return;
        }
        String cellValue;
        // 如单元格为公式，则保留该公式
        if (cell.getCellType() == CellType.FORMULA) {
            cellValue = cell.getCellFormula();
        } else {
            cellValue = cell.getStringCellValue();
        }
        if (StringUtils.isEmpty(cellValue)) {
            return;
        }
        boolean flag = false;
        prefixKey = StringUtils.isEmpty(prefixKey) ? "" : (prefixKey + ".");
        for (Map.Entry<String, String> current : map.entrySet()) {
            // 循环所有，因为可能一行有多个占位符
            String template = "{{" + prefixKey + current.getKey() + "}}";
            if (cellValue.contains(template)) {
                cellValue = cellValue.replace(template, current.getValue());
                flag = true;
            }
        }
        if (flag) {
            cell.setCellValue(cellValue);
        }
    }


}
