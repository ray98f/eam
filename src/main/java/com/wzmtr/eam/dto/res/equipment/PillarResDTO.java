package com.wzmtr.eam.dto.res.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class PillarResDTO {

    @ApiModelProperty(value = "统一编号")
    private String recId;

    @ApiModelProperty(value = "供电区间")
    private String powerSupplySection;

    @ApiModelProperty(value = "支柱号")
    private String pillarNumber;

    @ApiModelProperty(value = "锚编号")
    private String anchorNumber;

    @ApiModelProperty(value = "区间站")
    private String intervalStation;

    @ApiModelProperty(value = "下一个支柱号")
    private String nextPillarNumber;

    @ApiModelProperty(value = "腕臂柱安装图号")
    private String wristarmInstallationNumber;

    @ApiModelProperty(value = "回流线安装图号")
    private String backflowInstallationNumber;

    @ApiModelProperty(value = "里程DK")
    private String mileageDk;

    @ApiModelProperty(value = "接触线导高[mm]")
    private String contactWire;

    @ApiModelProperty(value = "接触线拉出值[mm]")
    private String pulloutValue;

    @ApiModelProperty(value = "侧面限界[mm]")
    private String sideClearance;

    @ApiModelProperty(value = "外轨超高[mm]")
    private String outerrailSuperelevation;

    @ApiModelProperty(value = "结构高度[mm]")
    private String structuralHeight;

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
