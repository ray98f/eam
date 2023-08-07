package com.wzmtr.eam.dto.res.spareAndCarVideo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 11:03
 */
@Data
public class CarVideoResDTO {
    @ApiModelProperty(value = "调阅记录号")
    private String recId;
    @ApiModelProperty(value = "申请ID")
    private String applyNo;
    @ApiModelProperty(value = "车组号")
    private String trainNo;
    @ApiModelProperty(value = "申请调阅原因")
    private String applyReason;
    @ApiModelProperty(value = "调阅性质")
    private String applyType;
    @ApiModelProperty(value = "申请人")
    private String applierId;
    @ApiModelProperty(value = "申请人手机")
    private String applierMobPhone;
    @ApiModelProperty(value = "申请部门")
    private String applyDeptCode;
    @ApiModelProperty(value = "申请部门负责人")
    private String applyDeptLeader;
    @ApiModelProperty(value = "申请时间")
    private String applyTime;
    @ApiModelProperty(value = "视频起始时间")
    private String videoStartTime;
    @ApiModelProperty(value = "视频截止时间")
    private String videoEndTime;
    @ApiModelProperty(value = "诊断/值班工程师")
    private String engineerId;
    @ApiModelProperty(value = "维保经理")
    private String repairManagerId;
    @ApiModelProperty(value = "调度人")
    private String dispatchUserId;
    @ApiModelProperty(value = "检修调度时间")
    private String dispatchTime;
    @ApiModelProperty(value = "派工人")
    private String workerId;
    @ApiModelProperty(value = "派工时间")
    private String workTime;
    @ApiModelProperty(value = "关闭人")
    private String closerId;
    @ApiModelProperty(value = "关闭时间")
    private String closeTime;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "状态")
    private String recStatus;

}
