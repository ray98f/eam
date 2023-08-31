package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/22 17:14
 */
@ApiModel
@Data
public class FaultConditionResDTO {
    @ApiModelProperty(value = "正线运营故障")
    private String ZX;
    @ApiModelProperty(value = "出入库非运营故障")
    private String CRK;
    @ApiModelProperty(value = "预防性检修故障")
    private String YF;
    @ApiModelProperty(value = "故障总数")
    private String ZS;
    private String NOYF;
    private String SC;
    @ApiModelProperty(value = "主要子系统")
    private String moduleName;
    @ApiModelProperty(value = "合同指标")
    private String contractZB;
    @ApiModelProperty(value = "是否达标")
    private String isDB;
    @ApiModelProperty(value = "实际指数")
    private String ZB;
}
