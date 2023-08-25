package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:18
 */
@Data
@ApiModel
public class FaultReportToMajorReqDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "专业")
    private String majorName;
    @ApiModelProperty(value = "系统")
    private String systemName;
    @ApiModelProperty(value = "设备分类")
    private String equipTypeName;
    @ApiModelProperty(value = "设备分类代码")
    private String equipTypeCode;
    @ApiModelProperty(value = "专业code")
    private String majorCode;
    @ApiModelProperty(value = "系统code")
    private String systemCode;
    @ApiModelProperty(value = "公司code")
    private String companyCode;
    @ApiModelProperty(value = "公司Name")
    private String companyName;
    @ApiModelProperty(value = "线别code")
    private String lineCode;
    @ApiModelProperty(value = "位置1")
    private String positionCode;
    @ApiModelProperty(value = "位置2")
    private String position2Name;
    @ApiModelProperty(value = "位置2编码")
    private String position2Code;
    @ApiModelProperty(value = "线别姓名")
    private String lineName;
    @ApiModelProperty(value = "故障分类（10-运营故障；20-自检故障；30-新线调试；40-正线故障；50-出库故障）")
    private String faultType;
    @ApiModelProperty(value = "故障模块")
    private String faultModule;
    @ApiModelProperty(value = "故障模块Id")
    private String faultModuleId;
    @ApiModelProperty(value = "来源编号")
    private String sourceCode = "";
    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;
    @ApiModelProperty(value = "发现人姓名")
    private String discovererName;
    @ApiModelProperty(value = "发现人工号")
    private String discovererId;
    @ApiModelProperty(value = "发现人手机号")
    private String discovererPhone;
    @ApiModelProperty(value = "知会OCC调度")
    private String maintenance;
    @ApiModelProperty(value = "提报时间")
    private String fillinTime;
    @ApiModelProperty(value = "提报人工号")
    private String fillinUserId;
    @ApiModelProperty(value = "提报人")
    private String fillinUserName;
    @ApiModelProperty(value = "提报部门Code")
    private String fillinDeptCode;
    @ApiModelProperty(value = "提报部门")
    private String fillinDeptName;
    @ApiModelProperty(value = "牵头部门")
    private String respDeptCode;
    @ApiModelProperty(value = "配合部门")
    private String assistDeptCode;
    @ApiModelProperty(value = "维修部门")
    private String repairDeptCode;
    @ApiModelProperty(value = "附件编号")
    private String docId;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    @ApiModelProperty(value = "状态")
    private String recStatus;

}
