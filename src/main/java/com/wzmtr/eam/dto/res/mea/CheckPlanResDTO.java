package com.wzmtr.eam.dto.res.mea;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author frp
 */
@Data
@ApiModel
public class CheckPlanResDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;
    
    @ApiModelProperty(value = "计量器具定检计划号")
    private String instrmPlanNo;
    
    @ApiModelProperty(value = "定抽检标识10:定检;20:抽检")
    private String instrmPlanType;
    
    @ApiModelProperty(value = "计量器具代码")
    private String equipCode;
    
    @ApiModelProperty(value = "计量器具名称")
    private String equipName;
    
    @ApiModelProperty(value = "定检计划时期")
    private String planPeriodMark;
    
    @ApiModelProperty(value = "定检开始日期")
    private String planBeginDate;
    
    @ApiModelProperty(value = "定检结束日期")
    private String planEndDate;
    
    @ApiModelProperty(value = "使用单位代码")
    private String useDeptCode;
    
    @ApiModelProperty(value = "使用单位")
    private String useDeptCname;
    
    @ApiModelProperty(value = "计划人工号")
    private String planCreaterNo;
    
    @ApiModelProperty(value = "计划人")
    private String planCreaterName;
    
    @ApiModelProperty(value = "计划提醒时间")
    private String planCreateTime;
    
    @ApiModelProperty(value = "计划状态10:提交;20:检测中;30:完成检测")
    private String planStatus;
    
    @ApiModelProperty(value = "备注")
    private String planNote;

    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;

    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;

    @ApiModelProperty(value = "公司代码")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

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

    @ApiModelProperty(value = "编制部门")
    private String editDeptCode;
}
