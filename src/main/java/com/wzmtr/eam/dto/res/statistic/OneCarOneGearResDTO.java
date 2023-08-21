package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Data
public class OneCarOneGearResDTO {
    @ApiModelProperty(value = "列车号")
    private String trainNo;
    @ApiModelProperty(value = "开始使用时间")
    private String startUseDate;
    @ApiModelProperty(value = "出厂日期")
    private String manufactureDate;


    /**
     * 统计数量
     ***/
    private Integer er2 = 0;
    private Integer er3 = 0;
    private Integer er4 = 0;
    private Integer er5 = 0;
    private Integer fmCount = 0;
    private Integer er1 = 0;
    private Integer maxMile = 0;
    private Integer maxkWh = 0;
    private Integer maxReEle = 0;
    private Integer minusMile = 0;
    private Integer minuskWh = 0;
    private Integer minusReEle = 0;
}