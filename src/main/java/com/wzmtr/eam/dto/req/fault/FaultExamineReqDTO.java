package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.dto.req.bpmn.ExamineReqDTO;
import com.wzmtr.eam.enums.SubmitType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:18
 */
@Data
@ApiModel
public class FaultExamineReqDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单编号")
    private String faultWorkNo;
    @ApiModelProperty(value = "故障跟踪编号")
    private String faultTrackNo;
    @ApiModelProperty(value = "故障分析编号")
    private String faultAnalysisNo;
    @ApiModelProperty(value = "流程相关参数")
    private ExamineReqDTO examineReqDTO;
    @ApiModelProperty(value = "是否提交他人审核")
    private Boolean reviewOrNot;
    @ApiModelProperty(value = "下一步的归属流程线")
    private String line;
    @ApiModelProperty(value = "跟踪结果")
    private String trackResult;
    @ApiModelProperty(value = "跟踪关闭人")
    private String trackCloserId;
    @ApiModelProperty(value = "跟踪关闭时间")
    private String trackCloseTime;
    @ApiModelProperty(value = "跟踪报告时间")
    private String trackReportTime;
}
