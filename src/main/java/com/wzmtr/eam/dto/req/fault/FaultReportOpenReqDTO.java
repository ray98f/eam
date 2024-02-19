package com.wzmtr.eam.dto.req.fault;

import lombok.Data;

import java.io.Serializable;

/**
 * 故障提报-开放 入参
 * @author  Ray
 * @version 1.0
 * @date 2023/12/11
 */
@Data
public class FaultReportOpenReqDTO implements Serializable {
    /**
     * 设备编码
     */
    private String equipCode;
    /**
     * 故障详情
     */
    private String faultDetail;
    /**
     * 报警时间
     */
    private String alamTime;
    /**
     * 线路
     */
    private String lineName;
    /**
     * 专业
     */
    private String majorName;
    /**
     * 故障类别
     */
    private String faultType;
    /**
     * 故障状态
     */
    private String faultStatus;
    /**
     * 故障等级
     */
    private String faultLevel;
    /**
     * 部件编码
     */
    private String partCode;
    /**
     * 故障编号
     */
    private String faultNo;
    /**
     * 故障工单编号
     */
    private String faultWorkNo;

}