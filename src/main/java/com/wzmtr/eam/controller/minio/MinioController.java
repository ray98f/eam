package com.wzmtr.eam.controller.minio;

import com.wzmtr.eam.entity.File;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.minio.MinioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:32
 */
@RestController
@RequestMapping("/minIO")
@Api(tags = "文件上传")
public class MinioController {

    private static final long NUM = 50 * 1024 * 1024;

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
}
