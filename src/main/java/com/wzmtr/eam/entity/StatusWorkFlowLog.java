package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusWorkFlowLog {

    /**
     * 待办主键ID
     */
    @ApiModelProperty(value = "待办主键ID")
    private String todoId;
    /**
     * 系统编码
     */
    @ApiModelProperty(value = "系统编码")
    private String syscode;
    /**
     * 1：新增2：更新 3：删除
     */
    @ApiModelProperty(value = "1：新增2：更新 3：删除")
    private String operId;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 代办人——用户ID
     */
    @ApiModelProperty(value = "代办人——用户ID")
    private String userId;
    /**
     * 1：待办 2：已办
     */
    @ApiModelProperty(value = "1：待办 2：已办")
    private String todoStatus;
    /**
     * EIP待办详情URL
     */
    @ApiModelProperty(value = "EIP待办详情URL")
    private String eipUrl;
    /**
     * 移动端待办详情URL
     */
    @ApiModelProperty(value = "移动端待办详情URL")
    private String phoneUrl;
    /**
     * 待办级别 1为最高级
     */
    @ApiModelProperty(value = "待办级别 1为最高级")
    private String kindType;
    /**
     * 待办/已办日期
     */
    @ApiModelProperty(value = "待办/已办日期")
    private String todoDate;
    /**
     * 当前步骤名称
     */
    @ApiModelProperty(value = "当前步骤名称")
    private String stepName;
    /**
     * 提交人员
     */
    @ApiModelProperty(value = "提交人员")
    private String lastStepUserId;
    /**
     * 任务到达时间
     */
    @ApiModelProperty(value = "任务到达时间")
    private String taskRcvTime;
    /**
     * 实际处理人ID
     */
    @ApiModelProperty(value = "实际处理人ID")
    private String processUserId;
    /**
     * 提交/审核意见
     */
    @ApiModelProperty(value = "提交/审核意见")
    private String auditOpinion;
    /**
     * 扩展字段1
     */
    @ApiModelProperty(value = "扩展字段1")
    private String ext1;
    /**
     * 扩展字段2
     */
    @ApiModelProperty(value = "扩展字段2")
    private String ext2;
    /**
     * 扩展字段3
     */
    @ApiModelProperty(value = "扩展字段3")
    private String ext3;
    /**
     * 扩展字段4
     */
    @ApiModelProperty(value = "扩展字段4")
    private String ext4;
    /**
     * 扩展字段5
     */
    @ApiModelProperty(value = "扩展字段5")
    private String ext5;
    /**
     * 扩展字段6
     */
    @ApiModelProperty(value = "扩展字段6")
    private String ext6;
    @ApiModelProperty(value = "流程Key")
    private String flowId;
    @ApiModelProperty(value = "业务ID")
    private String relateId;
}
