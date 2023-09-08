package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulWeekPlanResDTO {
    
    @ApiModelProperty(value = "记录ID")
    private String recId;
    
    @ApiModelProperty(value = "周计划编号")
    private String weekPlanCode;
    
    @ApiModelProperty(value = "计划名称")
    private String planName;
    
    @ApiModelProperty(value = "线路编码")
    private String lineNo;
    
    @ApiModelProperty(value = "线路名称")
    private String lineName;
    
    @ApiModelProperty(value = "位置1")
    private String position1Name;
    
    @ApiModelProperty(value = "位置1代码")
    private String position1Code;
    
    @ApiModelProperty(value = "设备专业编码")
    private String subjectCode;
    
    @ApiModelProperty(value = "设备专业名称")
    private String subjectName;
    
    @ApiModelProperty(value = "作业工班编码")
    private String workerGroupCode;
    
    @ApiModelProperty(value = "作业工班名称")
    private String workGroupName;
    
    @ApiModelProperty(value = "作业人员姓名")
    private String workerName;
    
    @ApiModelProperty(value = "作业人员工号")
    private String workerCode;
    
    @ApiModelProperty(value = "开始时间")
    private String firstBeginTime;
    
    @ApiModelProperty(value = "结束时间")
    private String planFinishTime;
    
    @ApiModelProperty(value = "计划状态")
    private String planStatus;
    
    @ApiModelProperty(value = "审批状态")
    private String trialStatus;
    
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;

    @ApiModelProperty(value = "说明")
    private String remark;
    
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "创建者")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改者")
    private String recRevisor;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;

    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;

    @ApiModelProperty(value = "扩展字段1")
    private String ext1;

    @ApiModelProperty(value = "扩展字段2")
    private String ext2;

    @ApiModelProperty(value = "扩展字段3")
    private String ext3;

    @ApiModelProperty(value = "扩展字段4")
    private String ext4;

    @ApiModelProperty(value = "扩展字段5")
    private String ext5;
}
