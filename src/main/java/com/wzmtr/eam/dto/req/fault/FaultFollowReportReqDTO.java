package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.entity.File;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 故障管理-故障跟踪工单报告请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/09
 */
@Data
@ApiModel
public class FaultFollowReportReqDTO {
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
     * 跟踪报告人工号
     */
    @ApiModelProperty(value = "跟踪报告人工号")
    private String reportUserId;
    /**
     * 跟踪报告人
     */
    @ApiModelProperty(value = "跟踪报告人")
    private String reportUserName;
    /**
     * 报告时间
     */
    @ApiModelProperty(value = "报告时间")
    private String reportTime;
    /**
     * 跟踪详情
     */
    @ApiModelProperty(value = "跟踪详情")
    private String reportDetail;
    /**
     * 附件id
     */
    @ApiModelProperty(value = "附件id")
    private String docId;
    /**
     * 附件列表
     */
    @ApiModelProperty(value = "附件列表")
    private List<File> docFile;
    /**
     * 跟踪报告阶段
     */
    @ApiModelProperty(value = "跟踪报告阶段")
    private Long step;
    /**
     * 报告审核人工号
     */
    @ApiModelProperty(value = "报告审核人工号")
    private String examineUserId;
    /**
     * 报告审核人
     */
    @ApiModelProperty(value = "报告审核人")
    private String examineUserName;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private String examineTime;
    /**
     * 审核意见
     */
    @ApiModelProperty(value = "审核意见")
    private String examineOpinion;
    /**
     * 审核状态 0待审核 1通过 2驳回
     */
    @ApiModelProperty(value = "审核状态 0待审核 1通过 2驳回")
    private String examineStatus;
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
}
