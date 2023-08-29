package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/22 17:14
 */
@Data
public class SystemFaultsResDTO {
    @ApiModelProperty(value = "正线")
    private Integer zx = 0;
    @ApiModelProperty(value = "出入库非运营故障")
    private Integer crk = 0;
    @ApiModelProperty(value = "预防性检修故障")
    private Integer yf = 0;
    @ApiModelProperty(value = "年月")
    private Integer yearMonth;
    @ApiModelProperty(value = "实际车公里数")
    private Integer miles;
}
