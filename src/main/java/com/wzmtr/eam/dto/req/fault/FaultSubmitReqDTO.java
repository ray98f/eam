package com.wzmtr.eam.dto.req.fault;

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
public class FaultSubmitReqDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单编号")
    private String faultWorkNo;
    private String faultTrackNo;
    @ApiModelProperty(value = "COMMIT 送审，PASS 审核通过")
    private SubmitType type;
    private String faultAnalysisNo;
    private String isCommit;
    private String comment;
    private String backOpinion;
    @ApiModelProperty(value = "userIds")
    private List<String> userIds;
}
