package com.wzmtr.eam.utils.convert;

import com.google.common.collect.Lists;
import com.wzmtr.eam.utils.FileUploadUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Pdf 转 PNG 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/18
 */
@Slf4j
public class Pdf2PngUtils {

    /**
     * dpi越大转换后越清晰，相对转换速度越慢
     */
    private static final Integer DPI = 100;

    /**
     * 转换后的图片类型
     */
    private static final String IMG_TYPE = "png";

    /**
     * `pdf` 转 `png`
     * @param pdfBytes pdf字节码
     * @return 转换后的png字节码
     */
    @SneakyThrows(Exception.class)
    public List<byte[]> pdf2Png(byte[] pdfBytes) {
        List<byte[]> result = new ArrayList<>();
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < document.getNumberOfPages(); ++i) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DPI);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, IMG_TYPE, out);
                result.add(out.toByteArray());
            }
        }
        return result;
    }

    /**
     * `pdf` 转 `png`
     * @param pdfBytes pdf字节码
     * @param imgRootPath 需转换的`png`文件路径
     * @return 图片文件数据列表
     */
    public List<File> pdf2Png(byte[] pdfBytes, String imgRootPath) {
        List<byte[]> pngBytesList = this.pdf2Png(pdfBytes);
        List<File> pngFileList = Lists.newArrayList();
        for (int i = 0; i < pngBytesList.size(); i++) {
            String imgPath = imgRootPath + "/" + (i + 1) + ".png";
            File pngFile = FileUploadUtils.writeFileContent(pngBytesList.get(i), imgPath);
            pngFileList.add(pngFile);
        }
        return pngFileList;
    }

}
