package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 10:35
 */
@Data
public class FaultDetailReqDTO {
    /**
     * 故障编号
     */
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    /**
     * 故障工单号
     */
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
}
