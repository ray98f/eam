package com.wzmtr.eam.impl.minio;

import com.wzmtr.eam.config.MinioConfig;
import com.wzmtr.eam.dto.req.FileReqDTO;
import com.wzmtr.eam.entity.File;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.minio.MinioService;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.FileUploadUtils;
import com.wzmtr.eam.utils.MinioUtils;
import com.wzmtr.eam.utils.TokenUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:38
 */
@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

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
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            client.putObject(args);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "上传失败");
        }
        String url = minioConfig.getImgPath() + "/" + bucket + "/" + fileName;
        FileReqDTO build = FileReqDTO.builder()
                .bucket(bucket)
                .fileName(fileName)
                .id(TokenUtil.getUuId())
                .oldName(oldName)
                .url(url)
                .recCreateTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS))
                .recCreator(TokenUtil.getCurrentPersonId())
                .build();
        fileMapper.insertFile(build);
        return fileMapper.getFile(url, bucket, oldName);
    }
}
