package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 故障管理-故障跟踪工单请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/09
 */
@Data
@ApiModel
public class FaultFollowReqDTO {
    /**
     * 记录编号
     */
    @ApiModelProperty(value = "记录编号")
    private String recId;
    /**
     * 故障跟踪单号
     */
    @ApiModelProperty(value = "故障跟踪单号")
    private String followNo;
    /**
     * 故障编号
     */
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    /**
     * 专业code
     */
    @ApiModelProperty(value = "专业code")
    private String majorCode;
    /**
     * 位置1
     */
    @ApiModelProperty(value = "位置1")
    private String positionCode;
    /**
     * 故障工单编号
     */
    @ApiModelProperty(value = "故障工单编号")
    private String faultWorkNo;
    /**
     * 跟踪原因
     */
    @ApiModelProperty(value = "跟踪原因")
    private String followReason;
    /**
     * 转跟踪人员工号
     */
    @ApiModelProperty(value = "转跟踪人员工号")
    private String followUserId;
    /**
     * 转跟踪人员
     */
    @ApiModelProperty(value = "转跟踪人员")
    private String followUserName;
    /**
     * 转跟踪时间
     */
    @ApiModelProperty(value = "转跟踪时间")
    private String followTime;
    /**
     * 跟踪工班长工号
     */
    @ApiModelProperty(value = "跟踪工班长工号")
    private String followLeaderId;
    /**
     * 跟踪工班长
     */
    @ApiModelProperty(value = "跟踪工班长")
    private String followLeaderName;
    /**
     * 跟踪周期
     */
    @ApiModelProperty(value = "跟踪周期")
    private Integer followCycle;
    /**
     * 跟踪开始时间
     */
    @ApiModelProperty(value = "跟踪开始时间")
    private String followStartDate;
    /**
     * 跟踪截止时间
     */
    @ApiModelProperty(value = "跟踪截止时间")
    private String followEndDate;
    /**
     * 跟踪关闭人工号
     */
    @ApiModelProperty(value = "跟踪关闭人工号")
    private String followCloserId;
    /**
     * 跟踪关闭人
     */
    @ApiModelProperty(value = "跟踪关闭人")
    private String followCloserName;
    /**
     * 跟踪关闭时间
     */
    @ApiModelProperty(value = "跟踪关闭时间")
    private String followCloseTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String recCreator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;
    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者")
    private String recRevisor;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;
    /**
     * 删除者
     */
    @ApiModelProperty(value = "删除者")
    private String recDeletor;
    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;
    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;
    /**
     * 跟踪状态
     */
    @ApiModelProperty(value = "跟踪状态")
    private String followStatus;
}
