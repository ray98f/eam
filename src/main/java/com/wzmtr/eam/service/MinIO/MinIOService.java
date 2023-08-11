package com.wzmtr.eam.service.MinIO;

import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 15:36
 */
public interface MinIOService {
    String upload(MultipartFile file);
}
