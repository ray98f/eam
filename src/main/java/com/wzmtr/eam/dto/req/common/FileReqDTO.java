package com.wzmtr.eam.dto.req.common;

import lombok.Builder;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 19:32
 */
@Data
@Builder
public class FileReqDTO {
    private String id;
    private String fileName;
    private String oldName;
    private String bucket;
    private String url;
    private String recCreator;
    private String recCreateTime;
}
