package com.wzmtr.eam.utils.convert;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.SaveFormat;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * html 转 png 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/17
 */
@Slf4j
public class Html2PngUtils {

    /**
     * html 转 png
     * @param htmlBytes: html字节码
     * @return 图片字节码数据列表
     */
    @SneakyThrows(Exception.class)
    public static List<byte[]> htmlBytes2PngBytes(byte[] htmlBytes) {
        Document doc = new Document(new ByteArrayInputStream(htmlBytes));
        ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.PNG);
        iso.setResolution(128);
        iso.setPrettyFormat(true);
        iso.setUseAntiAliasing(true);
        List<byte[]> pngList = Lists.newArrayList();
        for (int i = 0; i < doc.getPageCount(); i++) {
            iso.setPageIndex(i);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, iso);
            pngList.add(outputStream.toByteArray());
        }
        return pngList;
    }

    /**
     * html 转 png
     * @param htmlBytes:   html字节码
     * @param imgRootPath: 需转换的png文件路径
     * @return 图片文件数据列表
     */
    @SneakyThrows(Exception.class)
    public static List<File> htmlBytes2PngFileList(byte[] htmlBytes, String imgRootPath) {
        Document doc = new Document(new ByteArrayInputStream(htmlBytes));
        ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.PNG);
        iso.setResolution(128);
        iso.setPrettyFormat(true);
        iso.setUseAntiAliasing(true);

        List<File> pngList = Lists.newArrayList();
        for (int i = 0; i < doc.getPageCount(); i++) {
            String imgPath = imgRootPath + "/" + (i + 1) + ".png";
            iso.setPageIndex(i);
            doc.save(imgPath, iso);
            pngList.add(new File(imgPath));
        }
        return pngList;
    }

}
