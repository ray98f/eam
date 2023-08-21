package com.wzmtr.eam.entity;

import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/14 10:56
 */
@Data
public class File {
    private String oldName;
    private String newName;
    private String url;
    private String recCreator;
    private String recCreateTime;
    private String bucket;
    private String id;
}
