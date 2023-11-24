package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Data
public class FailureRateResDTO {
    @ApiModelProperty(value = "年月")
    private String yearMonth;
    @ApiModelProperty(value = "百分比")
    private String precent ;
}