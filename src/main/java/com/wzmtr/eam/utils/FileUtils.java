package com.wzmtr.eam.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

public class FileUtils {

    /** 字符常量：斜杠 {@code '/'} */
    public static final char SLASH = '/';

    /** 字符常量：反斜杠 {@code '\\'} */
    public static final char BACKSLASH = '\\';

    /**
     * 返回文件名
     *
     * @param filePath 文件
     * @return 文件名
     */
    public static String getName(String filePath) {
        if (null == filePath) {
            return null;
        }
        int len = filePath.length();
        if (0 == len) {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1))) {
            // 以分隔符结尾的去掉结尾分隔符
            len--;
        }
        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = filePath.charAt(i);
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1;
                break;
            }
        }
        return filePath.substring(begin, len);
    }

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }

    /**
     * MultipartFile转换成File
     * @param multipartFile
     * @return
     */
    public static File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String prefix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".") + 1);
            file = File.createTempFile(originalFilename.replace("." + prefix, ""), prefix);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
