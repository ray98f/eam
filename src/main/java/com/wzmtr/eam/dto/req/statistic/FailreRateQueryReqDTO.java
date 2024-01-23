package com.wzmtr.eam.dto.req.statistic;

import com.wzmtr.eam.enums.RateIndex;
import com.wzmtr.eam.enums.SystemType;
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
public class FailreRateQueryReqDTO {
    @ApiModelProperty(value = "时间开始")
    private String startTime;
    @ApiModelProperty(value = "时间结束")
    private String endTime;
    @ApiModelProperty(value = "指标可选项")
    private Set<RateIndex> index;
    @ApiModelProperty(value = "系统指标")
    private Set<SystemType> systemType;
    // dm.OrderType
}
