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
public class TrackResDTO {
    @ApiModelProperty(value = "记录ID")
    private String recId;
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "故障跟踪编号")
    private String faultTrackNo;
    @ApiModelProperty(value = "故障跟踪工单号")
    private String faultTrackWorkNo;
    @ApiModelProperty(value = "workerGroupCode")
    private String workerGroupCode;
    @ApiModelProperty(value = "workGroupName")
    private String workGroupName;
    @ApiModelProperty(value = "派工人工号")
    private String dispatchUserId;
    @ApiModelProperty(value = "派工时间")
    private String dispatchTime;
    @ApiModelProperty(value = "跟踪报告人工号")
    private String trackReporterId;
    @ApiModelProperty(value = "跟踪报告时间")
    private String trackReportTime;
    @ApiModelProperty(value = "跟踪结果")
    private String trackResult;
    @ApiModelProperty(value = "跟踪关闭人工号")
    private String trackCloserId;
    @ApiModelProperty(value = "跟踪关闭时间")
    private String trackCloseTime;
    @ApiModelProperty(value = "附件编号")
    private String docId;
    @ApiModelProperty(value = "备注")
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
