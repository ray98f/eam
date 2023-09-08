package com.wzmtr.eam.dto.res.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author frp
 */
@Data
@ApiModel
public class EquipmentChargeResDTO {

    @ApiModelProperty(value = "统一编号")
    private String recId;

    @ApiModelProperty(value = "设备编码")
    private String equipCode;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "专业")
    private String majorName;

    @ApiModelProperty(value = "系统")
    private String systemName;

    @ApiModelProperty(value = "设备类别")
    private String equipTypeName;

    @ApiModelProperty(value = "位置一")
    private String position1Name;

    @ApiModelProperty(value = "充电日期")
    private String chargeDate;

    @ApiModelProperty(value = "充电时长")
    private BigDecimal chargeDuration;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建者")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改者")
    private String recRevisor;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;

    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;

    @ApiModelProperty(value = "记录状态")
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
}
