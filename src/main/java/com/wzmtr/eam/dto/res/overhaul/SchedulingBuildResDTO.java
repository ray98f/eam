package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检修工单排期构建结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/16
 */
@Data
@ApiModel
public class SchedulingBuildResDTO {
    
    @ApiModelProperty(value = "记录ID")
    private String recId;
    
    @ApiModelProperty(value = "设备编号")
    private String equipCode;
    
    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "计划编号")
    private String planCode;

    @ApiModelProperty(value = "计划名称")
    private String planName;
    
    @ApiModelProperty(value = "排期名称")
    private String schedulingName;
    
    @ApiModelProperty(value = "日期")
    private String day;
    
    @ApiModelProperty(value = "是否触发 0否 1是")
    private String isTrigger;

    @ApiModelProperty(value = "类型 1一级修 2二级修")
    private String type;

    @ApiModelProperty(value = "日期类型")
    private Integer dateType;

    @ApiModelProperty(value = "是否已根据持续时间延期 0否 1是")
    private String isDuration;
}
