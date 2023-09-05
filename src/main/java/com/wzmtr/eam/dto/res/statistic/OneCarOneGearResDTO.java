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
    // 二级修(30天
    private Integer er2 = 0;
    //二级修(90天
    private Integer er3 = 0;
    //二级修(180天
    private Integer er4 = 0;
    //二级修(360天
    private Integer er5 = 0;
    //故障次数
    private Integer fmCount = 0;
    //一级修
    private Integer er1 = 0;
    //总里程数
    private Integer maxMile = 0;
    //牵引总能耗
    private Integer maxkWh = 0;
    //再生总电量
    private Integer maxReEle = 0;
    //时间段里程数
    private Integer minusMile = 0;
    //时间段牵引能耗
    private Integer minuskWh = 0;
    //时间段再生电量
    private Integer minusReEle = 0;
}