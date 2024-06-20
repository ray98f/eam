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
import java.util.List;

/**
 * 公共分类-文件管理
 * @author Li.Wang
 * @version 1.0
 * @date 2023/8/11 11:00
 */
@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    @Value("${pro.bucket-base}")
    private String bucketBase;

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
        if (!minioUtils.bucketExists(bucketBase)) {
            minioUtils.makeBucket(bucketBase);
        }
        String oldName = file.getOriginalFilename();
        String fileName = FileUploadUtils.extractFilename(file, bucket);
        try {
            @Cleanup
            InputStream inputStream = file.getInputStream();
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketBase)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            client.putObject(args);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "上传失败");
        }
        String url = minioConfig.getImgPath() + "/" + bucketBase + "/" + fileName;
        FileReqDTO build = FileReqDTO.builder()
                .bucket(bucketBase)
                .fileName(fileName)
                .id(TokenUtils.getUuId())
                .oldName(oldName)
                .url(url)
                .recCreateTime(DateUtils.getCurrentTime())
                .recCreator(TokenUtils.getCurrentPersonId())
                .build();
        fileMapper.insertFile(build);
        return fileMapper.getFile(url, bucketBase, oldName);
    }

    @Override
    public void clear(String bucketCode) throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (minioUtils.bucketExists(bucketCode)) {
            minioUtils.clearBucket(bucketCode);
            minioUtils.removeBucket(bucketCode);
        }
    }

    @Override
    public List<File> selectFileInfo(List<String> ids) {
        return fileMapper.selectFileInfo(ids);
    }
}
