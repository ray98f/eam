package com.wzmtr.eam.dto.req.secure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/7/31 18:31
 */
@Data
@ApiModel
public class SecureCheckDetailReqDTO {
    @ApiModelProperty(value = "检查问题单号")
    private String secRiskId;
}
