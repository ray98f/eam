package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/22 17:14
 */
@Data
public class SystemFaultsResDTO {
    @ApiModelProperty(value = "正线运营故障")
    private Integer zx = 0;
    @ApiModelProperty(value = "出入库非运营故障")
    private Integer crk = 0;
    @ApiModelProperty(value = "预防性检修故障")
    private Integer yf = 0;
    @ApiModelProperty(value = "年月")
    private Integer yearMonth;
    @ApiModelProperty(value = "故障总数")
    private Integer sum;
    @ApiModelProperty(value = "实际指数")
    private Integer noyfsum;
    @ApiModelProperty(value = "合同指数")
    private Integer miles;
    @ApiModelProperty(value = "是否达标")
    private Boolean compliance;
}
