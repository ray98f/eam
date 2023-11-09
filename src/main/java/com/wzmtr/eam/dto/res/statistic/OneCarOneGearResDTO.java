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
    private Long er2 =0L;
    //二级修(90天
    private Long er3 =0L;
    //二级修(180天
    private Long er4 =0L;
    //二级修(360天
    private Long er5 =0L;
    //故障次数
    private Long fmCount =0L;
    //一级修
    private Long er1 =0L;
    //总里程数
    private Long maxMile =0L;
    //牵引总能耗
    private Long maxkWh =0L;
    //再生总电量
    private Long maxReEle = 0L;
    //时间段里程数
    private Long minusMile =0L;
    //时间段牵引能耗
    private Long minuskWh =0L;
    //时间段再生电量
    private Long minusReEle =0L;
}