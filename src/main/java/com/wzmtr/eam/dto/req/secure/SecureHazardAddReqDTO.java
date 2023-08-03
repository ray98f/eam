package com.wzmtr.eam.dto.req.secure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/2 20:37
 */
@Data
@ApiModel
public class SecureHazardAddReqDTO {
    @ApiModelProperty(value = "记录编号")
    private String recId ;
    @ApiModelProperty(value = "隐患排查单号")
    private String riskId ;
    @ApiModelProperty(value = "发现日期")
    private String inspectDate ;
    @ApiModelProperty(value = "隐患等级")
    private String riskRank ;
    @ApiModelProperty(value = "隐患内容")
    private String riskDetail ;
    @ApiModelProperty(value = "报告部门")
    private String inspectDeptCode ;
    @ApiModelProperty(value = "报告人")
    private String inspectorCode ;
    @ApiModelProperty(value = "地点")
    private String positionDesc ;
    @ApiModelProperty(value = "位置一")
    private String position1Code ;
    @ApiModelProperty(value = "位置二")
    private String position2Code ;
    @ApiModelProperty(value = "位置三")
    private String position3 ;
    @ApiModelProperty(value = "位置补充说明")
    private String positionRemark ;
    @ApiModelProperty(value = "隐患照片")
    private String riskPic ;
    @ApiModelProperty(value = "整改通知下达部门")
    private String notifyDeptCode ;
    @ApiModelProperty(value = "整改措施")
    private String restoreDetail ;
    @ApiModelProperty(value = "计划完成日期")
    private String planDate ;
    @ApiModelProperty(value = "整改部门")
    private String restoreDeptCode ;
    @ApiModelProperty(value = "整改照片")
    private String restorePic ;
    @ApiModelProperty(value = "整改情况")
    private String restoreDesc ;
    @ApiModelProperty(value = "复查人")
    private String examinerCode ;
    @ApiModelProperty(value = "复查日期")
    private String examDate ;
    @ApiModelProperty(value = "完成情况")
    private String completionDetail ;
    @ApiModelProperty(value = "未整改原因及建议")
    private String undoneDesc ;
    @ApiModelProperty(value = "最终计划完成日期")
    private String lastPlanDate ;
    @ApiModelProperty(value = "追查人")
    private String lastCheckerId ;
    @ApiModelProperty(value = "追查日期")
    private String lastCheckDate ;
    @ApiModelProperty(value = "是否整改")
    private String isRestored ;
    @ApiModelProperty(value = "备注")
    private String planNote ;
    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId ;
    @ApiModelProperty(value = "工作流实例状态")
    private String workFlowInstStatus ;
    @ApiModelProperty(value = "记录状态")
    private String recStatus ;
    @ApiModelProperty(value = "创建者")
    private String recCreator ;
    @ApiModelProperty(value = "创建时间")
    private String recCreateTime ;
    @ApiModelProperty(value = "修改者")
    private String recRevisor ;
    @ApiModelProperty(value = "修改时间")
    private String recReviseTime ;
    @ApiModelProperty(value = "删除者")
    private String recDeletor ;
    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime ;
    @ApiModelProperty(value = "删除标志")
    private String deleteFlag ;
    @ApiModelProperty(value = "归档标记")
    private String archiveFlag ;
    @ApiModelProperty(value = "扩展字段1")
    private String ext1 ;
    @ApiModelProperty(value = "扩展字段2")
    private String ext2 ;
    @ApiModelProperty(value = "扩展字段3")
    private String ext3 ;
    @ApiModelProperty(value = "扩展字段4")
    private String ext4 ;
    @ApiModelProperty(value = "扩展字段5")
    private String ext5 ;
}
