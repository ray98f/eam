package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulPlanResDTO {
    
    @ApiModelProperty(value = "记录ID")
    private String recId;
    
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
    
    @ApiModelProperty(value = "规则编号")
    private String ruleCode;
    
    @ApiModelProperty(value = "规则名称")
    private String ruleName;
    
    @ApiModelProperty(value = "首次开始时间")
    private String firstBeginTime;
    
    @ApiModelProperty(value = "计划结束时间")
    private String planFinishTime;
    
    @ApiModelProperty(value = "上次执行时间")
    private String lastActionTime;
    
    @ApiModelProperty(value = "触发时间")
    private String trigerTime;
    
    @ApiModelProperty(value = "计划状态")
    private String planStatus;
    
    @ApiModelProperty(value = "计划编制人")
    private String planEditor;
    
    @ApiModelProperty(value = "计划编制日期")
    private String planEditorTime;
    
    @ApiModelProperty(value = "允许提前时间")
    private String allowDelayTime;
    
    @ApiModelProperty(value = "作业规定用时时间")
    private String taskFixedTime;
    
    @ApiModelProperty(value = "作业规定用时单位")
    private String taskFixedTimeUnit;
    
    @ApiModelProperty(value = "提前触发时间")
    private String beforeTriggerTime;
    
    @ApiModelProperty(value = "审批状态")
    private String trialStatus;
    
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;
    
    @ApiModelProperty(value = "父节点")
    private String parentNodeRecId;
    
    @ApiModelProperty(value = "节点层级")
    private Integer nodeLevel;
    
    @ApiModelProperty(value = "关联编码")
    private String relationCode;
    
    @ApiModelProperty(value = "计数标记")
    private Integer countFlag;
    
    @ApiModelProperty(value = "计数")
    private Integer count;

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

    @ApiModelProperty(value = "周计划编号")
    private String weekPlanCode;

    @ApiModelProperty(value = "公里标")
    private String kilometerScale;

    @ApiModelProperty(value = "施工类型")
    private String constructionType;
}
