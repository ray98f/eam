package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author frp
 */
@Data
@ApiModel
public class TrainMileReqDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "设备编码")
    private String equipCode;

    @ApiModelProperty(value = "车号")
    private String equipName;

    @ApiModelProperty(value = "里程(公里)")
    private String totalMiles;

    @ApiModelProperty(value = "牵引总能耗(kW·h)")
    private String totalTractionEnergy;

    @ApiModelProperty(value = "辅助总能耗(kW·h)")
    private String totalAuxiliaryEnergy;

    @ApiModelProperty(value = "再生总电量(kW·h)")
    private String totalRegenratedElectricity;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "维护时间")
    private String fillinTime;
}
