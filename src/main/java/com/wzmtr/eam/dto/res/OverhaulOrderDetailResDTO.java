package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulOrderDetailResDTO {

    private String objectId;
    
    @ApiModelProperty(value = "记录ID")
    private String recId;
    
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
    
    @ApiModelProperty(value = "作业人员工号")
    private String taskPersonId;
    
    @ApiModelProperty(value = "作业人员姓名")
    private String taskPersonName;
    
    @ApiModelProperty(value = "对象状态")
    private String objectStatus;
    
    @ApiModelProperty(value = "模板编号")
    private String templateId;
    
    @ApiModelProperty(value = "模板名称")
    private String templateName;
    
    @ApiModelProperty(value = "线路编码")
    private String lineNo;
    
    @ApiModelProperty(value = "线路名称")
    private String lineName;
    
    @ApiModelProperty(value = "位置1")
    private String position1Name;
    
    @ApiModelProperty(value = "位置2代码")
    private String position2Code;
    
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
    
    @ApiModelProperty(value = "检修工单")
    private String orderCode;
    
    @ApiModelProperty(value = "作业场")
    private String taskSpot;
    
    @ApiModelProperty(value = "停时")
    private String stopTime;
    
    @ApiModelProperty(value = "检修情况")
    private String repairStatus;
    
    @ApiModelProperty(value = "检修情况说明")
    private String repairDetail;
    
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    
    @ApiModelProperty(value = "结束时间")
    private String compliteTime;
    
    @ApiModelProperty(value = "检修人")
    private String repairPerson;
    
    @ApiModelProperty(value = "异常数量")
    private String abnormalNumber;
    
    @ApiModelProperty(value = "自检人")
    private String selfCheck;
    
    @ApiModelProperty(value = "自检时间")
    private String selfCheckTime;
    
    @ApiModelProperty(value = "自检备注")
    private String selfCheckRemark;
    
    @ApiModelProperty(value = "互检人")
    private String mutualCheck;
    
    @ApiModelProperty(value = "互检时间")
    private String mutualCheckTime;
    
    @ApiModelProperty(value = "互检备注")
    private String mutualCheckRemark;
    
    @ApiModelProperty(value = "专检人")
    private String expertCheck;
    
    @ApiModelProperty(value = "专检时间")
    private String expertCheckTime;
    
    @ApiModelProperty(value = "专检备注")
    private String expertCheckRemark;

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
