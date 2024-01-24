package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class StatusWorkFlowLog {

    @ApiModelProperty(value = "待办主键ID")
    private String todoId;

    @ApiModelProperty(value = "系统编码")
    private String syscode;

    @ApiModelProperty(value = "1：新增2：更新 3：删除")
    private String operId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "代办人——用户ID")
    private String userId;

    @ApiModelProperty(value = "1：待办 2：已办")
    private String todoStatus;

    @ApiModelProperty(value = "EIP待办详情URL")
    private String eipUrl;

    @ApiModelProperty(value = "移动端待办详情URL")
    private String phoneUrl;

    @ApiModelProperty(value = "待办级别 1为最高级")
    private String kindType;

    @ApiModelProperty(value = "待办/已办日期")
    private String todoDate;

    @ApiModelProperty(value = "当前步骤名称")
    private String stepName;

    @ApiModelProperty(value = "提交人员")
    private String lastStepUserId;

    @ApiModelProperty(value = "任务到达时间")
    private String taskRcvTime;

    @ApiModelProperty(value = "实际处理人ID")
    private String processUserId;

    @ApiModelProperty(value = "提交/审核意见")
    private String auditOpinion;

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

    @ApiModelProperty(value = "扩展字段6")
    private String ext6;

}
