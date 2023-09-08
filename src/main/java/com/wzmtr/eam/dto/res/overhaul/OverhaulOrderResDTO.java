package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulOrderResDTO {
    
    @ApiModelProperty(value = "记录ID")
    private String recId;
    
    @ApiModelProperty(value = "工单编号")
    private String orderCode;
    
    @ApiModelProperty(value = "计划编号")
    private String planCode;
    
    @ApiModelProperty(value = "计划名称")
    private String planName;
    
    @ApiModelProperty(value = "线路编码")
    private String lineNo;
    
    @ApiModelProperty(value = "线路名称")
    private String lineName;
    
    @ApiModelProperty(value = "位置1")
    private String position1Name;
    
    @ApiModelProperty(value = "位置1代码")
    private String position1Code;
    
    @ApiModelProperty(value = "设备专业编码")
    private String subjectCode;
    
    @ApiModelProperty(value = "设备专业名称")
    private String subjectName;
    
    @ApiModelProperty(value = "系统编码")
    private String systemCode;
    
    @ApiModelProperty(value = "系统名称")
    private String systemName;
    
    @ApiModelProperty(value = "设备类别编码")
    private String equipTypeCode;
    
    @ApiModelProperty(value = "设备类别名称")
    private String equipTypeName;
    
    @ApiModelProperty(value = "作业工班编码")
    private String workerGroupCode;
    
    @ApiModelProperty(value = "作业工班名称")
    private String workGroupName;
    
    @ApiModelProperty(value = "作业人员姓名")
    private String workerName;
    
    @ApiModelProperty(value = "作业人员工号")
    private String workerCode;
    
    @ApiModelProperty(value = "派工人工号")
    private String sendPersonId;
    
    @ApiModelProperty(value = "派工人姓名")
    private String sendPersonName;
    
    @ApiModelProperty(value = "派工时间")
    private String sendTime;
    
    @ApiModelProperty(value = "完工时间")
    private String ackTime;
    
    @ApiModelProperty(value = "确认人工号")
    private String ackPersonId;
    
    @ApiModelProperty(value = "确认人姓名")
    private String ackPersonName;
    
    @ApiModelProperty(value = "确认时间")
    private String confirTime;
    
    @ApiModelProperty(value = "计划开始时间")
    private String planStartTime;
    
    @ApiModelProperty(value = "计划完成时间")
    private String planEndTime;
    
    @ApiModelProperty(value = "实际开始时间")
    private String realStartTime;
    
    @ApiModelProperty(value = "实际完成时间")
    private String realEndTime;
    
    @ApiModelProperty(value = "工单当前状态")
    private String workStatus;
    
    @ApiModelProperty(value = "检修结果")
    private String workFinishStatus;
    
    @ApiModelProperty(value = "异常数量")
    private String abnormalNumber;
    
    @ApiModelProperty(value = "检修描述")
    private String workDetail;
    
    @ApiModelProperty(value = "开工时间")
    private String startworkTime;
    
    @ApiModelProperty(value = "作废人工号")
    private String cancelPersonId;
    
    @ApiModelProperty(value = "作废人姓名")
    private String cancelPersonName;
    
    @ApiModelProperty(value = "作废时间")
    private String cancelTime;
    
    @ApiModelProperty(value = "审批状态")
    private String trialStatus;
    
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;

    @ApiModelProperty(value = "说明")
    private String remark;
    
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

    @ApiModelProperty(value = "最后修改人")
    private String lastUpdatePerson;
}
