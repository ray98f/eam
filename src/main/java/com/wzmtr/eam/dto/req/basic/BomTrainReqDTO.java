package com.wzmtr.eam.dto.req.basic;

import lombok.Data;

/**
 * 车辆与Bom关联关系请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/02/06
 */
@Data
public class BomTrainReqDTO {
    /**
     * 统一编号
     */
    private String recId;
    /**
     * 设备编号
     */
    private String equipCode;
    /**
     * 车号
     */
    private String equipName;
    /**
     * 一级Bom编号
     */
    private String bomParentCode;
    /**
     * 一级Bom名称
     */
    private String bomParenName;
}
