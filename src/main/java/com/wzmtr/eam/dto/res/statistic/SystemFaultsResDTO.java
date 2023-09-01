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
    private String zx ;
    @ApiModelProperty(value = "出入库非运营故障")
    private String crk ;
    @ApiModelProperty(value = "预防性检修故障")
    private String yf ;
    @ApiModelProperty(value = "年月")
    private String yearMonth;
    @ApiModelProperty(value = "实际车公里数")
    private String miles;
}
