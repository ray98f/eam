package com.wzmtr.eam.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传工具类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/06/12
 */
@Slf4j
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

    /**
     * 多文件或目录压缩
     * 将`srcPath`目录以及其目录下的所有文件目录打包到`zipPath`+`suffixFileName`文件中
     * 采用hutool工具类进行打包文件
     * @param srcPath:        需打包的源目录
     * @param zipPath:        打包后的路径+文件后缀名
     * @param isWithSrcDir:   是否带目录显示 （true:表示带目录显示）
     * @param isDeleteSrcZip: 是否删除源目录
     * @return String
     */
    public static File zip(String srcPath, String zipPath, boolean isWithSrcDir, boolean isDeleteSrcZip) {
        File zipFile = ZipUtil.zip(srcPath, zipPath, isWithSrcDir);
        // 删除目录 -> 保证下次生成文件的时候不会累计上次留下的文件
        if (isDeleteSrcZip) {
            FileUploadUtils.deleteFileOrFolder(srcPath);
        }
        return zipFile;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     * @param fullFileOrDirPath: 要删除的目录或文件
     */
    public static void deleteFileOrFolder(String fullFileOrDirPath) {
        FileUtil.del(fullFileOrDirPath);
    }

    /**
     * 根据路径创建文件
     * @param fullFilePath: 文件生成路径
     * @return 文件信息
     */
    public static File touch(String fullFilePath) {
        return FileUtil.touch(fullFilePath);
    }

    /**
     * 解压
     * @param inputStream: 流
     * @param zipFilePath: zip文件路径
     * @param outFileDir:  解压后的目录路径
     * @param isDeleteZip: 是否删除源zip文件
     * @return 解压后的文件File信息
     */
    @SneakyThrows(Exception.class)
    public static File unzip(InputStream inputStream, String zipFilePath, String outFileDir, boolean isDeleteZip) {
        // zip压缩文件
        File zipFile = FileUtil.newFile(zipFilePath);
        // 写入文件
        FileUtils.copyInputStreamToFile(inputStream, zipFile);
        // 编码方式 "UTF-8" 、"GBK" 【注： gbk编码才能解决报错: java.lang.IllegalArgumentException: MALFORMED】
        File outFile = ZipUtil.unzip(zipFilePath, outFileDir, Charset.forName("GBK"));
        // 删除zip -> 保证下次解压后的文件数据不会累计上次解压留下的文件
        if (isDeleteZip) {
            FileUploadUtils.deleteFileOrFolder(zipFilePath);
        }
        return outFile;
    }

    /**
     * 读取文件内容
     * @param file: 文件数据
     * @return 文件内容
     */
    public static String readFileContent(File file) {
        return FileUtil.readUtf8String(file);
    }

    /**
     * 读取文件内容
     * @param filePath: 文件路径
     * @return 文件内容
     */
    public static String readFileContent(String filePath) {
        return FileUtil.readUtf8String(filePath);
    }

    /**
     * 读取文件数据
     * @param filePath: 文件路径
     * @return 文件字节码
     */
    public static byte[] readBytes(String filePath) {
        return FileUtil.readBytes(filePath);
    }

    /**
     * 写入文件内容
     * @param fileContent: 文件内容
     * @param filePath:    文件路径
     * @return 文件信息
     */
    @SneakyThrows(Exception.class)
    public static File writeFileContent(String fileContent, String filePath) {
        return FileUtil.writeUtf8String(fileContent, filePath);
    }

    /**
     * 字节码写入文件
     * @param data:     字节码
     * @param filePath: 文件路径
     * @return 文件信息
     */
    @SneakyThrows(Exception.class)
    public static File writeFileContent(byte[] data, String filePath) {
        return FileUtil.writeBytes(data, filePath);
    }
}