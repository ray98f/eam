package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.enums.SubmitType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:18
 */
@Data
@ApiModel
public class FaultCancelReqDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "故障工单编号")
    private String faultWorkNo;
    private String orderRecId;
}
