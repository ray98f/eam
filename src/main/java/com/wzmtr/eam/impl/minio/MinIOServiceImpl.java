package com.wzmtr.eam.impl.minio;

import com.wzmtr.eam.config.MinioConfig;
import com.wzmtr.eam.entity.File;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.MinIO.MinIOService;
import com.wzmtr.eam.utils.FileUploadUtils;
import com.wzmtr.eam.utils.MinioUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:38
 */
@Service
@Slf4j
public class MinIOServiceImpl implements MinIOService {

    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private MinioClient client;
    @Autowired
    private MinioConfig minioConfig;
    @Autowired
    private FileMapper fileMapper;

    @Override
    public File upload(MultipartFile file, String bizCode) {
        if (!minioUtils.bucketExists(bizCode)) {
            minioUtils.makeBucket(bizCode);
        }
        String oldName = file.getOriginalFilename();
        String fileName = FileUploadUtils.extractFilename(file);
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bizCode)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            log.error("upload error", e);
        }
        String url = minioConfig.getImgPath() + "/" + bizCode + "/" + fileName;
        fileMapper.insertFile(url, bizCode, oldName);
        return fileMapper.getFile(url, bizCode, oldName);
    }
}
