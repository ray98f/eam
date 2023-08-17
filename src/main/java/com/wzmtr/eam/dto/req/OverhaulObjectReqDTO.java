package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulObjectReqDTO {
    
    @ApiModelProperty(value = "记录ID")
    private String recId;

    @ApiModelProperty(value = "周计划编号")
    private String weekPlanCode;
    
    @ApiModelProperty(value = "计划编号")
    private String planCode;
    
    @ApiModelProperty(value = "计划名称")
    private String planName;
    
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    
    @ApiModelProperty(value = "作业内容")
    private String taskContent;
    
    @ApiModelProperty(value = "作业区域")
    private String taskArea;
    
    @ApiModelProperty(value = "作业需求")
    private String taskRequest;
    
    @ApiModelProperty(value = "作业备注")
    private String taskRemark;
    
    @ApiModelProperty(value = "模板编号")
    private String templateId;
    
    @ApiModelProperty(value = "模板名称")
    private String templateName;
    
    @ApiModelProperty(value = "作业场")
    private String taskSpot;

    @ApiModelProperty(value = "说明")
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
