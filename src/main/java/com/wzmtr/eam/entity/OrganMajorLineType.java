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

    //是否为工班长标识,DM012和DM051都归类为这个标识
    private String isDM012;
}
