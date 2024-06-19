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
 * Word 转 JPEG 工具类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/19
 */
@Slf4j
public class Word2JpegUtils {

    /**
     * `word` 转 `jpeg`
     * @param wordBytes: word字节码数据
     * @return 图片字节码数据列表
     */
    @SneakyThrows(Exception.class)
    public static List<byte[]> wordBytes2JpegBytes(byte[] wordBytes) {
        Document doc = new Document(new ByteArrayInputStream(wordBytes));
        ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.JPEG);
        iso.setResolution(128);
        iso.setPrettyFormat(true);
        iso.setUseAntiAliasing(true);

        List<byte[]> jpegList = Lists.newArrayList();
        for (int i = 0; i < doc.getPageCount(); i++) {
            iso.setPageIndex(i);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, iso);
            jpegList.add(outputStream.toByteArray());
        }
        return jpegList;
    }

    /**
     * `word` 转 `jpeg`
     * @param wordBytes:   word字节码数据
     * @param imgRootPath: 生成图片根路径
     * @return 图片文件数据列表
     */
    @SneakyThrows(Exception.class)
    public static List<File> wordBytes2JpegFileList(byte[] wordBytes, String imgRootPath) {
        Document doc = new Document(new ByteArrayInputStream(wordBytes));
        ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.JPEG);
        iso.setResolution(128);
        iso.setPrettyFormat(true);
        iso.setUseAntiAliasing(true);

        List<File> jpegList = Lists.newArrayList();
        for (int i = 0; i < doc.getPageCount(); i++) {
            String imgPath = imgRootPath + "/" + (i + 1) + ".jpg";
            iso.setPageIndex(i);
            doc.save(imgPath, iso);
            jpegList.add(new File(imgPath));
        }
        return jpegList;
    }

}
