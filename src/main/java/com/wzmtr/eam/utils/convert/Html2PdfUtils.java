package com.wzmtr.eam.utils.convert;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Html 转 Pdf 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/17
 */
@Slf4j
public class Html2PdfUtils {

    /**
     * html 转 pdf
     * @param htmlBytes: html字节码
     * @return 生成的pdf字节码
     */
    @SneakyThrows(Exception.class)
    public static byte[] htmlBytes2PdfBytes(byte[] htmlBytes) {
        Document document = new Document(new ByteArrayInputStream(htmlBytes));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream, SaveFormat.PDF);
        // 返回生成的pdf字节码
        return outputStream.toByteArray();
    }

    /**
     * html 转 pdf
     * @param htmlBytes:   html字节码
     * @param pdfFilePath: 需转换的pdf文件路径
     * @return 生成的pdf文件数据
     */
    @SneakyThrows(Exception.class)
    public static File htmlBytes2PdfFile(byte[] htmlBytes, String pdfFilePath) {
        Document document = new Document(new ByteArrayInputStream(htmlBytes));
        document.save(pdfFilePath, SaveFormat.PDF);
        return new File(pdfFilePath);
    }

}
