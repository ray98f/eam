package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 10:35
 */
@ApiModel
@Data
public class FaultEqCheckReqDTO {
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
}
