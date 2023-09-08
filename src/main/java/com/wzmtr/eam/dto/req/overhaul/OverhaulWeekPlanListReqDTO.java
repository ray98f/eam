package com.wzmtr.eam.dto.req.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulWeekPlanListReqDTO {

    @ApiModelProperty(value = "记录ID")
    private String recId;

    @ApiModelProperty(value = "周计划编号")
    private String weekPlanCode;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "线路编码")
    private String lineNo;

    @ApiModelProperty(value = "设备专业编码")
    private String subjectCode;

    @ApiModelProperty(value = "开始时间")
    private String firstBeginTime;

    @ApiModelProperty(value = "开始时间")
    private String firstBeginTime1;

    @ApiModelProperty(value = "审批状态")
    private String trialStatus;

    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;

    @ApiModelProperty(value = "作业工班编码")
    private String workerGroupCode;
}
