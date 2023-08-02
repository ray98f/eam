package com.wzmtr.eam.dto.req.secure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/2 15:59
 */
@Data
@ApiModel
public class SecureDangerSourceDetailReqDTO {
    @ApiModelProperty(value = "危险源记录单号")
    private String dangerRiskId = " ";
}
