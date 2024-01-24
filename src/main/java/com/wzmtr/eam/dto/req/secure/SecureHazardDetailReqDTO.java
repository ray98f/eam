package com.wzmtr.eam.dto.req.secure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Author: Li.Wang
 * Date: 2023/8/2 20:37
 */
@Data
@ApiModel
public class SecureHazardDetailReqDTO  {
    @NotNull
    @ApiModelProperty(value = "隐患排查单号")
    private String riskId ;
}
