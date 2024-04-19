package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检修工单排期结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/15
 */
@Data
@ApiModel
public class SchedulingResDTO {

    /**
     * 记录ID
     */
    @ApiModelProperty(value = "记录ID")
    private String recId;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String equipCode;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String equipName;
    /**
     * 计划编号
     */
    @ApiModelProperty(value = "计划编号")
    private String planCode;
    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String planName;
    /**
     * 排期名称
     */
    @ApiModelProperty(value = "排期名称")
    private String schedulingName;
    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    private String day;
    /**
     * 是否触发 0否 1是
     */
    @ApiModelProperty(value = "是否触发 0否 1是")
    private String isTrigger;
    /**
     * 类型 1一级修 2二级修
     */
    @ApiModelProperty(value = "类型 1一级修 2二级修")
    private String type;
    /**
     * 二级修包类型
     */
    @ApiModelProperty(value = "二级修包类型")
    private String packageType;
}
