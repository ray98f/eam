package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Data
public class InspectionJobListResDTO {
    @ApiModelProperty(value = "当天总公里数")
    private String dmer3km;
    @ApiModelProperty(value = "日期")
    private String dmer3date;
}