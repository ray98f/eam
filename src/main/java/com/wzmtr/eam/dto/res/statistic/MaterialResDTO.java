package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:27
 */
@Data
public class MaterialResDTO {
    @ApiModelProperty(value = "检修作业时间")
    private String realTime;
    @ApiModelProperty(value = "计划名称")
    private String planName;
    @ApiModelProperty(value = "出库单号")
    private String deliveryNo;
    @ApiModelProperty(value = "检修工单号")
    private String relationNo;
    @ApiModelProperty(value = "物资编码")
    private String batchNo;
    @ApiModelProperty(value = "物资编码")
    private String matcode;
    @ApiModelProperty(value = "物资名称")
    private String matname;
    @ApiModelProperty(value = "领用数量")
    private String deliveryNum;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "规格型号")
    private String specifi;
    @ApiModelProperty(value = "领用人姓名")
    private String pickName;
    @ApiModelProperty(value = "计量单位")
    private String unit;
}