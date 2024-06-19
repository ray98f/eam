package com.wzmtr.eam.utils.convert;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Word 转 Pdf 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/19
 */
@Slf4j
public class Word2PdfUtils {

    /**
     * `word` 转 `pdf`
     * @param wordBytes: word字节码
     * @return 生成的`pdf`字节码
     */
    @SneakyThrows(Exception.class)
    public static byte[] wordBytes2PdfBytes(byte[] wordBytes) {
        Document document = new Document(new ByteArrayInputStream(wordBytes));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream, SaveFormat.PDF);
        // 返回生成的`pdf`字节码
        return outputStream.toByteArray();
    }

    /**
     * `word` 转 `pdf`
     * @param wordBytes:   word字节码
     * @param pdfFilePath: 需转换的`pdf`文件路径
     * @return 生成的`pdf`文件数据
     */
    @SneakyThrows(Exception.class)
    public static File wordBytes2PdfFile(byte[] wordBytes, String pdfFilePath) {
        Document document = new Document(new ByteArrayInputStream(wordBytes));
        document.save(pdfFilePath, SaveFormat.PDF);
        return new File(pdfFilePath);
    }

}
