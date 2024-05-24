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
public class OverhaulOrderListReqDTO {

    @ApiModelProperty(value = "标记")
    private String objectFlag;

    @ApiModelProperty(value = "记录ID")
    private String recId;

    @ApiModelProperty(value = "工单编号")
    private String orderCode;

    @ApiModelProperty(value = "计划编号")
    private String planCode;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "对象名称")
    private String objectName;

    @ApiModelProperty(value = "线路编码")
    private String lineNo;

    @ApiModelProperty(value = "位置1代码")
    private String position1Code;

    @ApiModelProperty(value = "设备专业编码")
    private String subjectCode;

    @ApiModelProperty(value = "系统编码")
    private String systemCode;

    @ApiModelProperty(value = "作业工班编码")
    private String workerGroupCode;

    @ApiModelProperty(value = "工单当前状态")
    private String workStatus;

    @ApiModelProperty(value = "工单当前状态1")
    private String workStatus1;

    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;

    @ApiModelProperty(value = "检修结果")
    private String workFinishStatus;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "删除人")
    private String newTime;
    /**
     * 外部调用标识
     */
    private String tenant;

    /**
     * 检修情况
     */
    @ApiModelProperty(value = "检修情况")
    private String workDetail;

    /**
     * 用户专业
     */
    @ApiModelProperty(value = "用户专业")
    private List<String> majors;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 用户组织机构id
     */
    @ApiModelProperty(value = "用户组织机构id")
    private String officeAreaId;
}
