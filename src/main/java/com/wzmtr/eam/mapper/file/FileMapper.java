package com.wzmtr.eam.mapper.file;

import com.wzmtr.eam.dto.req.FileReqDTO;
import com.wzmtr.eam.entity.File;

/**
 * Author: Li.Wang
 * Date: 2023/8/14 11:00
 */
public interface FileMapper {


    File getFile(String url, String bucket, String oldName);

    void insertFile(FileReqDTO dto);
}
