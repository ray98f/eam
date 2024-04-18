package com.wzmtr.eam.impl.minio;

import com.wzmtr.eam.config.MinioConfig;
import com.wzmtr.eam.dto.req.common.FileReqDTO;
import com.wzmtr.eam.entity.File;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.minio.MinioService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.FileUploadUtils;
import com.wzmtr.eam.utils.MinioUtils;
import com.wzmtr.eam.utils.TokenUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:38
 */
@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    @Value("${pro.name}")
    private String proName;

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
        if (!minioUtils.bucketExists(proName)) {
            minioUtils.makeBucket(proName);
        }
        String oldName = file.getOriginalFilename();
        String fileName = FileUploadUtils.extractFilename(file, bucket);
        try {
            @Cleanup
            InputStream inputStream = file.getInputStream();
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(proName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            client.putObject(args);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "上传失败");
        }
        String url = minioConfig.getImgPath() + "/" + proName + "/" + fileName;
        FileReqDTO build = FileReqDTO.builder()
                .bucket(proName)
                .fileName(fileName)
                .id(TokenUtils.getUuId())
                .oldName(oldName)
                .url(url)
                .recCreateTime(DateUtils.getCurrentTime())
                .recCreator(TokenUtils.getCurrentPersonId())
                .build();
        fileMapper.insertFile(build);
        return fileMapper.getFile(url, proName, oldName);
    }

    @Override
    public void clear(String bucketCode) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioUtils.clearBucket(bucketCode);
        minioUtils.removeBucket(bucketCode);
    }
}
