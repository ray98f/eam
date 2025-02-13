package com.wzmtr.eam.dto.req.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:36
 */
@ApiModel
@Data
public class CarFaultQueryReqDTO  {
    @ApiModelProperty(value = "车辆codes")
    private Set<String> objectCode;
    @ApiModelProperty(value = "时间周期开始")
    private String startTime;
    @ApiModelProperty(value = "时间周期结束")
    private String endTime;
}
