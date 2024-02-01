package com.wzmtr.eam.mapper.file;

import com.wzmtr.eam.dto.req.common.FileReqDTO;
import com.wzmtr.eam.entity.File;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/14 11:00
 */
public interface FileMapper {

    File getFile(String url, String bucket, String oldName);

    void insertFile(FileReqDTO dto);

    List<File> selectFileInfo(List<String> ids);
}
