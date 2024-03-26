package com.wzmtr.eam.dto.req.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulPlanListReqDTO {

    @ApiModelProperty(value = "ids")
    private List<String> ids;

    @ApiModelProperty(value = "标记")
    private String objectFlag;

    @ApiModelProperty(value = "记录ID")
    private String recId;

    @ApiModelProperty(value = "计划编号")
    private String planCode;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "线路编码")
    private String lineNo;

    @ApiModelProperty(value = "位置1代码")
    private String position1Code;

    @ApiModelProperty(value = "设备专业编码")
    private String subjectCode;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "设备类别编码")
    private String equipTypeCode;

    @ApiModelProperty(value = "作业工班编码")
    private String workerGroupCode;

    @ApiModelProperty(value = "计划状态")
    private String planStatus;

    @ApiModelProperty(value = "审批状态")
    private String trialStatus;

    @ApiModelProperty(value = "审批状态1")
    private String trialStatus1;

    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;

    @ApiModelProperty(value = "首次开始")
    private String firstBegin;

    @ApiModelProperty(value = "父节点id")
    private String parentNodeRecId1;

    @ApiModelProperty(value = "父节点code")
    private String parentNode;

    @ApiModelProperty(value = "关联编码")
    private String relationCode;

    @ApiModelProperty(value = "删除人")
    private String recDeletor;

    @ApiModelProperty(value = "施工类型")
    private String constructionType;

    @ApiModelProperty(value = "节点层级")
    private String nodeLevel2;

    @ApiModelProperty(value = "周计划编号")
    private String weekPlanCode;
}
