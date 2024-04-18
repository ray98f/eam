package com.wzmtr.eam.service.minio;

import com.wzmtr.eam.entity.File;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:36
 */
public interface MinioService {
    File upload(MultipartFile file, String bucketCode);

    void clear(String bucketCode) throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
