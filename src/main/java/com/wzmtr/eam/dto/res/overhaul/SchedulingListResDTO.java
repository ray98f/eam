package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检修工单排期列表结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/17
 */
@Data
@ApiModel
public class SchedulingListResDTO {

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
     * 排期列表
     */
    @ApiModelProperty(value = "排期列表")
    private List<SchedulingResDTO> schedulingList;
}
