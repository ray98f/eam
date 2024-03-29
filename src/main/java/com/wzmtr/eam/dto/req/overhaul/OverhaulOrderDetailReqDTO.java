package com.wzmtr.eam.dto.req.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulOrderDetailReqDTO {

    /**
     * 记录ID
     */
    @ApiModelProperty(value = "记录ID")
    private String recId;
    /**
     * 计划编号
     */
    @ApiModelProperty(value = "计划编号")
    private String planCode;
    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private String planName;
    /**
     * 对象编码
     */
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    /**
     * 对象名称
     */
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    /**
     * 作业内容
     */
    @ApiModelProperty(value = "作业内容")
    private String taskContent;
    /**
     * 作业区域
     */
    @ApiModelProperty(value = "作业区域")
    private String taskArea;
    /**
     * 作业需求
     */
    @ApiModelProperty(value = "作业需求")
    private String taskRequest;
    /**
     * 作业备注
     */
    @ApiModelProperty(value = "作业备注")
    private String taskRemark;
    /**
     * 作业人员工号
     */
    @ApiModelProperty(value = "作业人员工号")
    private String taskPersonId;
    /**
     * 作业人员姓名
     */
    @ApiModelProperty(value = "作业人员姓名")
    private String taskPersonName;
    /**
     * 对象状态
     */
    @ApiModelProperty(value = "对象状态")
    private String objectStatus;
    /**
     * 模板编号
     */
    @ApiModelProperty(value = "模板编号")
    private String templateId;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private String templateName;
    /**
     * 线路编码
     */
    @ApiModelProperty(value = "线路编码")
    private String lineNo;
    /**
     * 线路名称
     */
    @ApiModelProperty(value = "线路名称")
    private String lineName;
    /**
     * 位置1
     */
    @ApiModelProperty(value = "位置1")
    private String position1Name;
    /**
     * 位置2代码
     */
    @ApiModelProperty(value = "位置2代码")
    private String position2Code;
    /**
     * 设备专业编码
     */
    @ApiModelProperty(value = "设备专业编码")
    private String subjectCode;
    /**
     * 设备专业名称
     */
    @ApiModelProperty(value = "设备专业名称")
    private String subjectName;
    /**
     * 系统编码
     */
    @ApiModelProperty(value = "系统编码")
    private String systemCode;
    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String systemName;
    /**
     * 设备类别编码
     */
    @ApiModelProperty(value = "设备类别编码")
    private String equipTypeCode;
    /**
     * 设备类别名称
     */
    @ApiModelProperty(value = "设备类别名称")
    private String equipTypeName;
    /**
     * 检修工单
     */
    @ApiModelProperty(value = "检修工单")
    private String orderCode;
    /**
     * 作业场
     */
    @ApiModelProperty(value = "作业场")
    private String taskSpot;
    /**
     * 停时
     */
    @ApiModelProperty(value = "停时")
    private String stopTime;
    /**
     * 检修情况
     */
    @ApiModelProperty(value = "检修情况")
    private String repairStatus;
    /**
     * 检修情况说明
     */
    @ApiModelProperty(value = "检修情况说明")
    private String repairDetail;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String compliteTime;
    /**
     * 检修人
     */
    @ApiModelProperty(value = "检修人")
    private String repairPerson;
    /**
     * 异常数量
     */
    @ApiModelProperty(value = "异常数量")
    private String abnormalNumber;
    /**
     * 自检人
     */
    @ApiModelProperty(value = "自检人")
    private String selfCheck;
    /**
     * 自检时间
     */
    @ApiModelProperty(value = "自检时间")
    private String selfCheckTime;
    /**
     * 自检备注
     */
    @ApiModelProperty(value = "自检备注")
    private String selfCheckRemark;
    /**
     * 互检人
     */
    @ApiModelProperty(value = "互检人")
    private String mutualCheck;
    /**
     * 互检时间
     */
    @ApiModelProperty(value = "互检时间")
    private String mutualCheckTime;
    /**
     * 互检备注
     */
    @ApiModelProperty(value = "互检备注")
    private String mutualCheckRemark;
    /**
     * 专检人
     */
    @ApiModelProperty(value = "专检人")
    private String expertCheck;
    
    @ApiModelProperty(value = "专检时间")
    private String expertCheckTime;
    
    @ApiModelProperty(value = "专检备注")
    private String expertCheckRemark;

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
