package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检修工单排期规则结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/15
 */
@Data
@ApiModel
public class SchedulingRuleResDTO {
    
    @ApiModelProperty(value = "设备编号")
    private String equipCode;
    
    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "计划编号")
    private String planCode;

    @ApiModelProperty(value = "计划名称")
    private String planName;
    
    @ApiModelProperty(value = "计划首次开始时间")
    private String firstBeginTime;
    
    @ApiModelProperty(value = "规则编号")
    private String ruleCode;
    
    @ApiModelProperty(value = "规则名称")
    private String ruleDetalName;

    @ApiModelProperty(value = "周期")
    private Long period;

    @ApiModelProperty(value = "提前时间")
    private Long beforeTime;
}
