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
public class TrainMileageResDTO {


    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "设备编号")
    private String equipCode;

    @ApiModelProperty(value = "车号")
    private String equipName;

    @ApiModelProperty(value = "里程(公里)")
    private BigDecimal totalMiles;

    @ApiModelProperty(value = "增加里程(公里)")
    private BigDecimal milesIncrement;

    @ApiModelProperty(value = "填报时间")
    private String fillinTime;

    @ApiModelProperty(value = "填报人")
    private String fillinUserId;

    @ApiModelProperty(value = "填报人姓名")
    private String fillinUserName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "创建人")
    private String recRevisor;

    @ApiModelProperty(value = "创建时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;

    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;

    @ApiModelProperty(value = "状态")
    private String recStatus;

    @ApiModelProperty(value = "扩展字段1")
    private String ext1;

    @ApiModelProperty(value = "扩展字段2")
    private String ext2;

    @ApiModelProperty(value = "扩展字段3")
    private String ext3;

    @ApiModelProperty(value = "扩展字段4")
    private String ext4;

    @ApiModelProperty(value = "扩展字段5")
    private String ext5;

    @ApiModelProperty(value = "牵引总能耗(kW·h)")
    private BigDecimal totalTractionEnergy;

    @ApiModelProperty(value = "辅助总能耗(kW·h)")
    private BigDecimal totalAuxiliaryEnergy;

    @ApiModelProperty(value = "再生总电量(kW·h)")
    private BigDecimal totalRegenratedElectricity;

    @ApiModelProperty(value = "牵引能耗增量")
    private BigDecimal tractionIncrement;

    @ApiModelProperty(value = "辅助能耗增量")
    private BigDecimal auxiliaryIncrement;

    @ApiModelProperty(value = "再生电量增量")
    private BigDecimal regenratedIncrement;
}
