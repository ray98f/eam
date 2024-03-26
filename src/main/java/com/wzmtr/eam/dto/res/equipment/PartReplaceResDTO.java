package com.wzmtr.eam.dto.res.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class PartReplaceResDTO {
    
    @ApiModelProperty(value = "记录编号")
    private String recId;
    
    @ApiModelProperty(value = "故障工单编号")
    private String faultWorkNo;
    
    @ApiModelProperty(value = "设备代码")
    private String equipCode;
    
    @ApiModelProperty(value = "设备名称")
    private String equipName;
    
    @ApiModelProperty(value = "作业单位")
    private String orgType;
    
    @ApiModelProperty(value = "作业人员")
    private String operator;
    
    @ApiModelProperty(value = "更换配件代码")
    private String replacementNo;
    
    @ApiModelProperty(value = "更换配件名称")
    private String replacementName;
    
    @ApiModelProperty(value = "更换原因")
    private String replaceReason;
    
    @ApiModelProperty(value = "旧配件编号")
    private String oldRepNo;
    
    @ApiModelProperty(value = "新配件编号")
    private String newRepNo;
    
    @ApiModelProperty(value = "更换所用时间")
    private String operateCostTime;
    
    @ApiModelProperty(value = "处理日期")
    private String replaceDate;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "附件编号")
    private String docId;

    @ApiModelProperty(value = "车厢号")
    private String carNo;

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

    @ApiModelProperty(value = "扩展字段6")
    private String ext6;

    @ApiModelProperty(value = "扩展字段7")
    private String ext7;
}
