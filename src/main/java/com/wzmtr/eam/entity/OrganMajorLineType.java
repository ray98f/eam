package com.wzmtr.eam.entity;

import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 14:18
 */
@Data
public class OrganMajorLineType {
    private String orgCode;
    private String orgName;
    private String majorCode;
    private String lineCode;
    private String loginName;
    private String userName;
    private String isDM012;//是否为工班长
}
