package com.wzmtr.eam.bizobject;

import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/9/22 10:02
 */
@Data
public class PartBO {
    //更换配件名称
    private String replacementName;
    private String oldRepNo;
    private String newRepNo;
    private String operateCostTime;
}
