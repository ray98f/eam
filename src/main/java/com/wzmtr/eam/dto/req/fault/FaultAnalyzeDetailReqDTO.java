package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 10:35
 */
@Data
public class FaultAnalyzeDetailReqDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
    @ApiModelProperty(value = "故障分析编号")
    private String faultAnalysisNo;
}
