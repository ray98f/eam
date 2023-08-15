package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:47
 */
@Data
@ApiModel
public class TrackQueryResDTO {
    @ApiModelProperty(value = "记录编号")
    private String recId;
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "故障跟踪编号")
    private String faultTrackNo;
    @ApiModelProperty(value = "故障分析编号")
    private String faultAnalysisNo;
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单编号")
    private String faultWorkNo;
    @ApiModelProperty(value = "跟踪原因")
    private String trackReason;
    @ApiModelProperty(value = "转跟踪人员工号")
    private String trackUserId;
    @ApiModelProperty(value = "转跟踪人员")
    private String trackUserName;
    @ApiModelProperty(value = "转跟踪时间")
    private String trackTime;
    @ApiModelProperty(value = "跟踪期限")
    private Integer trackPeriod = 0;
    @ApiModelProperty(value = "跟踪周期")
    private Integer trackCycle = 0;
    @ApiModelProperty(value = "跟踪开始时间")
    private String trackStartDate;
    @ApiModelProperty(value = "跟踪截止时间")
    private String trackEndDate;
    @ApiModelProperty(value = "转跟踪人员")
    private String trackReporterId;
    @ApiModelProperty(value = "转跟踪报告时间")
    private String trackReportTime;
    @ApiModelProperty(value = "跟踪结果")
    private String trackResult;
    @ApiModelProperty(value = "跟踪关闭人工号")
    private String trackCloserId;
    @ApiModelProperty(value = "跟踪关闭时间")
    private String trackCloseTime;
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus;
    @ApiModelProperty(value = "附件编号")
    private String docId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "对象code")
    private String objectCode;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "位置code")
    private String positionCode;
    @ApiModelProperty(value = "位置code")
    private String positionName;
    @ApiModelProperty(value = "故障现象code")
    private String faultDisplayCode;
    @ApiModelProperty(value = "故障现象详情")
    private String faultDisplayDetail;
    @ApiModelProperty(value = "故障原因code")
    private String faultReasonCode;
    @ApiModelProperty(value = "故障原因")
    private String faultReasonDetail;
    @ApiModelProperty(value = "线路")
    private String lineCode;
    @ApiModelProperty(value = "专业")
    private String majorCode;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;

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


}
