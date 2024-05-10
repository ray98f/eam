package com.wzmtr.eam.mapper.file;

import com.wzmtr.eam.dto.req.common.FileReqDTO;
import com.wzmtr.eam.entity.File;

import java.util.List;

/**
 * 公共分类-文件管理
 * @author Li.Wang
 * @version 1.0
 * @date 2023/8/14 11:00
 */
public interface FileMapper {

    /**
     * 获取文件信息
     * @param url 链接
     * @param bucket 文件桶编号
     * @param oldName 文件原始名称
     * @return 文件信息
     */
    File getFile(String url, String bucket, String oldName);

    /**
     * 文件上传后新增数据库
     * @param dto 文件信息
     */
    void insertFile(FileReqDTO dto);

    /**
     * 根据文件ids获取文件列表
     * @param ids 文件ids
     * @return 文件列表
     */
    List<File> selectFileInfo(List<String> ids);
}
