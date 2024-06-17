package com.wzmtr.eam.utils.convert;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Excel 转 Pdf 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/17
 */
@Slf4j
public class Excel2PdfUtils {

    /**
     * `excel` 转 `pdf`
     * @param excelBytes: html字节码
     * @return 生成的`pdf`文件流
     */
    @SneakyThrows(Exception.class)
    public static byte[] excelBytes2PdfBytes(byte[] excelBytes) {
        Workbook workbook = new Workbook(new ByteArrayInputStream(excelBytes));
        // 设置pdf保存的格式以及强制所有列都在同一页
        PdfSaveOptions pso = new PdfSaveOptions();
        pso.setAllColumnsInOnePagePerSheet(true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.save(outputStream, pso);
        // workbook.save(outputStream, SaveFormat.PDF);
        // 返回生成的pdf文件字节码
        return outputStream.toByteArray();
    }

    /**
     * `excel` 转 `pdf`
     * @param excelBytes:  html字节码
     * @param pdfFilePath: 待生成的`pdf`文件路径
     * @return 生成的`pdf`文件数据
     */
    @SneakyThrows(Exception.class)
    public static File excelBytes2PdfFile(byte[] excelBytes, String pdfFilePath) {
        Workbook workbook = new Workbook(new ByteArrayInputStream(excelBytes));
        // 设置pdf保存的格式以及强制所有列都在同一页
        PdfSaveOptions pso = new PdfSaveOptions();
        pso.setAllColumnsInOnePagePerSheet(true);
        workbook.save(pdfFilePath, pso);
        return new File(pdfFilePath);
    }

}
