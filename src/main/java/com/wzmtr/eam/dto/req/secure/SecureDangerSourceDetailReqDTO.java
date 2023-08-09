package com.wzmtr.eam.dto.req.secure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Author: Li.Wang
 * Date: 2023/8/2 15:59
 */
@Data
@ApiModel
public class SecureDangerSourceDetailReqDTO {
    @NotNull
    @ApiModelProperty(value = "危险源记录单号")
    private String dangerRiskId;
}
