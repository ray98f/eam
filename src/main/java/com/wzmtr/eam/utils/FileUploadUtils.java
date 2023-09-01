package com.wzmtr.eam.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

public class FileUploadUtils {
    /**
     * 默认大小 10M
     */
    public static final long DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;


    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file) {
        String fileName;
        String extension = getExtension(file);
        fileName = DateUtil.datePath() + UUID.randomUUID() + "." + extension;
        return fileName;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }
}