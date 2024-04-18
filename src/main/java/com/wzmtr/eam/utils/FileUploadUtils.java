package com.wzmtr.eam.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

public class FileUploadUtils {

    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file, String bucket) {
        String extension = getExtension(file);
        return bucket + "/" + DateUtils.datePath() + "/" + UUID.randomUUID() + "." + extension;
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