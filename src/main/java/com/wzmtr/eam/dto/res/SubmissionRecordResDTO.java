package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class SubmissionRecordResDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;
    
    @ApiModelProperty(value = "检测单号")
    private String checkNo;
    
    @ApiModelProperty(value = "送检单号")
    private String sendVerifyNo;
    
    @ApiModelProperty(value = "计量器具检验计划号")
    private String instrmPlanNo;
    
    @ApiModelProperty(value = "计量器具检测计划明细表REC_ID")
    private String planDetailRecId;
    
    @ApiModelProperty(value = "检测校准单位")
    private String verifyDept;
    
    @ApiModelProperty(value = "备注")
    private String verifyNote;

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
    
    @ApiModelProperty(value = "送检委托人")
    private String sendConsignerName;
    
    @ApiModelProperty(value = "送检委托人电话")
    private String sendConsignerTele;
    
    @ApiModelProperty(value = "送检接收人")
    private String sendReceiverName;
    
    @ApiModelProperty(value = "送检接收人电话")
    private String sendReceiverTele;
    
    @ApiModelProperty(value = "返送人")
    private String backReturnName;
    
    @ApiModelProperty(value = "返送人电话")
    private String backReturnTele;
    
    @ApiModelProperty(value = "返还接收人")
    private String backReceiverName;
    
    @ApiModelProperty(value = "返还接收人电话")
    private String backConsignerTele;
    
    @ApiModelProperty(value = "送检日期")
    private String sendVerifyDate;
    
    @ApiModelProperty(value = "返还日期")
    private String verifyBackDate;
    
    @ApiModelProperty(value = "送检单状态10:送检中;20:完成检测;")
    private String sendVerifyStatus;
    
    @ApiModelProperty(value = "编制部门")
    private String editDeptCode;
}
