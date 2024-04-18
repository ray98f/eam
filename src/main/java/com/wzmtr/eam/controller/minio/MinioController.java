package com.wzmtr.eam.controller.minio;

import com.wzmtr.eam.entity.File;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.minio.MinioService;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:32
 */
@RestController
@RequestMapping("/minIO")
@Api(tags = "文件上传")
public class MinioController {

    private static final long NUM = 50L * 1024L * 1024L;

    @Autowired
    private MinioService minioService;

    @ApiOperation(value = "上传")
    @PostMapping("/upload")
    public DataResponse<File> upload(@RequestParam MultipartFile file, String bucketCode) {
        if (file.getSize() > NUM) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "上传文件大小超出限制");
        }
        return DataResponse.of(minioService.upload(file, bucketCode));
    }

    /**
     * 清空桶
     * @param bucketCode 桶名
     * @return 成功
     * @throws ServerException 异常
     * @throws InsufficientDataException 异常
     * @throws ErrorResponseException 异常
     * @throws IOException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeyException 异常
     * @throws InvalidResponseException 异常
     * @throws XmlParserException 异常
     * @throws InternalException 异常
     */
    @ApiOperation(value = "清空桶")
    @GetMapping("/clear")
    public DataResponse<T> clear(@RequestParam String bucketCode) throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioService.clear(bucketCode);
        return DataResponse.success();
    }
}
