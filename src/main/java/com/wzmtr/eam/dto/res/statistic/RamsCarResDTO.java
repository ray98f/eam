package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/22 17:14
 */
@Data
public class RamsCarResDTO {
    @ApiModelProperty(value = "故障数")
    private String faultNum;
    @ApiModelProperty(value = "晚点数")
    private String affect11;
    @ApiModelProperty(value = "晚点指数")
    private String affect12;
    @ApiModelProperty(value = "是否达标")
    private String affect13;

    @ApiModelProperty(value = "不适合继续服务")
    private String affect21;
    @ApiModelProperty(value = "不适合继续服务指数")
    private String affect22;
    @ApiModelProperty(value = "是否达标")
    private String affect23;
    private String millionMiles;
}
