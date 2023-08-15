package com.wzmtr.eam.service.MinIO;

import com.wzmtr.eam.entity.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:36
 */
public interface MinIOService {
    File upload(MultipartFile file, String bucketCode);
}
