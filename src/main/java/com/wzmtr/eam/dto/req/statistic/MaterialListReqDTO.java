package com.wzmtr.eam.dto.req.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:36
 */
@ApiModel
@Data
public class MaterialListReqDTO {
    @ApiModelProperty(value = "列车号")
    private String trainNo;
    @ApiModelProperty(value = "设备号")
    private String equipCode;
    @ApiModelProperty(value = "时间开始")
    private String startTime;
    @ApiModelProperty(value = "时间结束")
    private String endTime;
    @ApiModelProperty(value = "检修选项")
    private String planName;
    @ApiModelProperty(value = "物料名称")
    private String matName;
    // dm.OrderType
}
