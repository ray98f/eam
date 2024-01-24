package com.wzmtr.eam.dto.req.statistic;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:36
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class OneCarOneGearQueryReqDTO extends PageReqDTO {
    @ApiModelProperty(value = "时间开始")
    private String startTime;
    @ApiModelProperty(value = "时间结束")
    private String endTime;
    @ApiModelProperty(value = "列车号",required = true)
    private String equipName;
}
