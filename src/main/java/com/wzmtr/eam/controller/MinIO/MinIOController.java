package com.wzmtr.eam.controller.MinIO;

import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.service.MinIO.MinIOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:32
 */
@RestController
@RequestMapping("/minIO")
@Api(tags = "文件上传")
public class MinIOController {
    @Autowired
    private MinIOService minIOService;

    @ApiOperation(value = "上传")
    @PostMapping("/upload")
    public DataResponse<String> upload(@RequestParam MultipartFile file) {
        return DataResponse.of(minIOService.upload(file));
    }
}
