package com.wzmtr.eam.impl.MinIO;

import com.wzmtr.eam.service.MinIO.MinIOService;
import io.minio.MinioClient;
import io.minio.ObjectWriteArgs;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:38
 */
@Service
@Slf4j
public class MinIOServiceImpl implements MinIOService {
    @Value("${minio.url}")
    private String readUrl;
    @Resource
    private MinioClient minioClient;

    @Override
    public String upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .object(filename)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE).build());
        } catch (Exception e) {
            log.error("upload error!", e);
        }
        // 返回可访问的图片链接
        return readUrl + "/" + filename;
    }
    // private String getFileName(String filename){
    //     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    //     // 设置存储对象名称
    //     String dir = sdf.format(new Date());
    //     int idx = filename.lastIndexOf(".");
    //     if (idx >= 0) {
    //         String ext = filename.substring(idx+1);
    //         String name = System.currentTimeMillis() + filename.substring(0, idx);
    //         filename = XString.md5(name) + "." + ext;
    //     }
    //     return dir + "/" +filename;
    // }
}
