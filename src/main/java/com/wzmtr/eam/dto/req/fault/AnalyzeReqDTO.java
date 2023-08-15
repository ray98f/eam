package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 15:22
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class AnalyzeReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "分析报告状态")
    private String recStatus;
    @ApiModelProperty(value = "线别")
    private String lineCode;
    @ApiModelProperty(value = "位置")
    private String positionCode;
    @ApiModelProperty(value = "专业")
    private String majorCode;
    @ApiModelProperty(value = "故障发现开始时间")
    private String discoveryStartTime;
    @ApiModelProperty(value = "故障发现结束时间")
    private String discoveryEndTime;
    @ApiModelProperty(value = "频次（10-偶尔；20-一般；30-频发)")
    private String frequency;
    @ApiModelProperty(value = "责任部门")
    private String respDeptCode;
    @ApiModelProperty(value = "故障影响")
    private String affectCodes;
}