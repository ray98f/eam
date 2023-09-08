package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class EquipmentRoomReqDTO {

    @ApiModelProperty(value = "统一编号")
    private String recId;

    @ApiModelProperty(value = "设备房编码")
    private String equipRoomCode;

    @ApiModelProperty(value = "设备房名称")
    private String equipRoomName;

    @ApiModelProperty(value = "专业编码")
    private String subjectCode;

    @ApiModelProperty(value = "专业名称")
    private String subjectName;

    @ApiModelProperty(value = "线别编码")
    private String lineCode;

    @ApiModelProperty(value = "位置一编码")
    private String position1Code;

    @ApiModelProperty(value = "位置一名称")
    private String position1Name;

    @ApiModelProperty(value = "位置二编码")
    private String position2Code;

    @ApiModelProperty(value = "位置二名称")
    private String position2Name;

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
