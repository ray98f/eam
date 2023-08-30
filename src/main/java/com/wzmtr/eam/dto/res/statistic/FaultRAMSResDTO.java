package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/30 15:23
 */
@Data
public class FaultRAMSResDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "故障分类dm.affect")
    private String faultType;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;
    @ApiModelProperty(value = "提报人工号")
    private String fillinUserId;
    @ApiModelProperty(value = "提报部门")
    private String fillinDeptCode;
    @ApiModelProperty(value = "到达时间")
    private String arrivalTime;
    @ApiModelProperty(value = "填单用户")
    private String fillinUser;
    @ApiModelProperty(value = "维修结束时间")
    private String repairEndTime;
    @ApiModelProperty(value = "维修时间")
    private String repairTime;
    @ApiModelProperty(value = "完工人工号")
    private String reportFinishUserId;
    @ApiModelProperty(value = "完工用户")
    private String reportFinishUser;
    @ApiModelProperty(value = "故障状态dm.faultStatus")
    private String orderStatus;
    @ApiModelProperty(value = "故障处理详情")
    private String faultActionDetail;
    @ApiModelProperty(value = "故障工作单号")
    private String faultWorkNo;
    @ApiModelProperty(value = "影响范围")
    private String affect;
    @ApiModelProperty(value = "故障分析号")
    private String faultAnalysisNo;
    @ApiModelProperty(value = "更换部件名称")
    private String replacementName;
    @ApiModelProperty(value = "旧件序列号")
    private String oldRepNo;
    @ApiModelProperty(value = "新件序列号")
    private String newRepNo;
    @ApiModelProperty(value = "部件更换时间")
    private String operateCostTime;
}