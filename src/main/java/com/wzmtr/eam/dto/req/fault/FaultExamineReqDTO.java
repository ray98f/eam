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
    private String faultTrackNo;
    private String faultAnalysisNo;
    private String isCommit;
    private String comment;
    private ExamineReqDTO examineReqDTO;
    @ApiModelProperty(value = "是否提交他人审核")
    private Boolean reviewOrNot;
}
