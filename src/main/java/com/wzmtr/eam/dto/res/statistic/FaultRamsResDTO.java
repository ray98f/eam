package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/30 15:23
 */
@Data
@ApiModel
public class FaultRamsResDTO {
    /**
     * 故障编号
     */
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    /**
     * 对象名称
     */
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    /**
     * 故障分类dm.affect
     */
    @ApiModelProperty(value = "故障分类dm.affect")
    private String faultType;
    /**
     * 故障详情
     */
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    /**
     * 发现时间
     */
    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;
    /**
     * 提报人工号
     */
    @ApiModelProperty(value = "提报人工号")
    private String fillinUserId;
    /**
     * 提报部门
     */
    @ApiModelProperty(value = "提报部门")
    private String fillinDeptCode;
    /**
     * 到达时间
     */
    @ApiModelProperty(value = "到达时间")
    private String arrivalTime;
    /**
     * 填单用户
     */
    @ApiModelProperty(value = "填单用户")
    private String fillinUser;
    /**
     * 维修结束时间
     */
    @ApiModelProperty(value = "维修结束时间")
    private String repairEndTime;
    /**
     * 维修时间
     */
    @ApiModelProperty(value = "维修时间")
    private String repairTime;
    /**
     * 完工人工号
     */
    @ApiModelProperty(value = "完工人工号")
    private String reportFinishUserId;
    /**
     * 完工用户
     */
    @ApiModelProperty(value = "完工用户")
    private String reportFinishUser;
    /**
     * 故障状态dm.faultStatus
     */
    @ApiModelProperty(value = "故障状态dm.faultStatus")
    private String orderStatus;
    /**
     * 故障处理详情
     */
    @ApiModelProperty(value = "故障处理详情")
    private String faultActionDetail;
    /**
     * 故障工作单号
     */
    @ApiModelProperty(value = "故障工作单号")
    private String faultWorkNo;
    /**
     * 影响范围
     */
    @ApiModelProperty(value = "影响范围")
    private String affect;
    /**
     * 故障分析号
     */
    @ApiModelProperty(value = "故障分析号")
    private String faultAnalysisNo;
    /**
     * 更换部件名称
     */
    @ApiModelProperty(value = "更换部件名称")
    private String replacementName;
    /**
     * 旧件序列号
     */
    @ApiModelProperty(value = "旧件序列号")
    private String oldRepNo;
    /**
     * 新件序列号
     */
    @ApiModelProperty(value = "新件序列号")
    private String newRepNo;
    /**
     * 部件更换时间
     */
    @ApiModelProperty(value = "部件更换时间")
    private String operateCostTime;
}