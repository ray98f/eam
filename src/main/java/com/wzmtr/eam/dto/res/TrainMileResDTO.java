package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author frp
 */
@Data
@ApiModel
public class TrainMileResDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "设备编码")
    private String equipCode;

    @ApiModelProperty(value = "车号")
    private String equipName;

    @ApiModelProperty(value = "里程(公里)")
    private BigDecimal totalMiles;

    @ApiModelProperty(value = "牵引总能耗(kW·h)")
    private BigDecimal totalTractionEnergy;

    @ApiModelProperty(value = "辅助总能耗(kW·h)")
    private BigDecimal totalAuxiliaryEnergy;

    @ApiModelProperty(value = "再生总电量(kW·h)")
    private BigDecimal totalRegenratedElectricity;

    @ApiModelProperty(value = "维护时间")
    private String fillinTime;
}
