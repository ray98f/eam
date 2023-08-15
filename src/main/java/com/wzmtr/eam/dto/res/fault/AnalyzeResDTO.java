package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:47
 */
@Data
@ApiModel
public class AnalyzeResDTO {
    @ApiModelProperty(value = "记录编号")
    private String recId;
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "故障分析编号")
    private String faultAnalysisNo;
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
    @ApiModelProperty(value = "主设备编码")
    private String mainEquipCode;
    @ApiModelProperty(value = "主设备名称")
    private String mainEquipName;
    @ApiModelProperty(value = "派工时间")
    private String dispatchTime;
    @ApiModelProperty(value = "工班")
    private String workClass;
    @ApiModelProperty(value = "跟踪报告时间")
    private String trackReportTime;
    @ApiModelProperty(value = "专业代码")
    private String majorCode;
    @ApiModelProperty(value = "专业")
    private String majorName;
    @ApiModelProperty(value = "线路")
    private String lineCode;
    @ApiModelProperty(value = "恢复时间")
    private String recoveryTime;
    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;
    @ApiModelProperty(value = "故障影响")
    private String affectCodes;
    @ApiModelProperty(value = "频次（10-偶尔；20-一般；30-频发)")
    private String frequency;
    @ApiModelProperty(value = "生产厂家")
    private String manufacture;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    @ApiModelProperty(value = "故障调查及处置")
    private String faultProcessDetail;
    @ApiModelProperty(value = "故障原因")
    private String faultReasonDetail;
    @ApiModelProperty(value = "故障现象code")
    private String faultDisplayCode;
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayDetail;
    @ApiModelProperty(value = "暴露问题")
    private String problemDescr;
    @ApiModelProperty(value = "整改措施")
    private String improveDetail;
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    @ApiModelProperty(value = "workFlowInstStatus")
    private String workFlowInstStatus;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "分析报告状态")
    private String recStatus;
    @ApiModelProperty(value = "位置")
    private String recCreator;
    @ApiModelProperty(value = "牵头部门")
    private String respDeptCode;

    @ApiModelProperty(value = "故障等级")
    private String faultLevel;
    @ApiModelProperty(value = "系统")
    private String systemCode;
    private String recCreateTime;
    private String recRevisor;
    private String positionCode;
    private String position;
    private String recReviseTime;
    private String recDeletor;
    private String recDeleteTime;
    private String deleteFlag;
    private String archiveFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
}
