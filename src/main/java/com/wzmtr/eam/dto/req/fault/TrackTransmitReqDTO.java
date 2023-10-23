package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 15:22
 */
@Data
@ApiModel
public class TrackTransmitReqDTO {
    @ApiModelProperty(value = "跟踪单号")
    private String faultTrackNo;
    @ApiModelProperty(value = "故障跟踪工单号")
    private String faultTrackWorkNo;
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
    @ApiModelProperty(value = "流程ID")
    private String workFlowInstId;
}
