package com.wzmtr.eam.impl.minio;

import com.wzmtr.eam.config.MinioConfig;
import com.wzmtr.eam.dto.req.FileReqDTO;
import com.wzmtr.eam.entity.File;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.MinIO.MinIOService;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.FileUploadUtils;
import com.wzmtr.eam.utils.MinioUtils;
import com.wzmtr.eam.utils.TokenUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jdk.nashorn.internal.parser.Token;
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
    public File upload(MultipartFile file, String bucket) {
        if (!minioUtils.bucketExists(bucket)) {
            minioUtils.makeBucket(bucket);
        }
        String oldName = file.getOriginalFilename();
        String fileName = FileUploadUtils.extractFilename(file);
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            log.error("upload error", e);
        }
        String url = minioConfig.getImgPath() + "/" + bucket + "/" + fileName;
        FileReqDTO build = FileReqDTO.builder()
                .bucket(bucket)
                .fileName(fileName)
                .id(TokenUtil.getUuId())
                .oldName(oldName)
                .recCreateTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS))
                .recCreator(TokenUtil.getCurrentPersonId())
                .build();
        fileMapper.insertFile(build);
        return fileMapper.getFile(url, bucket, oldName);
    }
}
