package com.wzmtr.eam.dto.req.secure;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Li.Wang
 * Date: 2023/8/2 9:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SecureDangerSourceListReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "危险源记录单号")
    private String dangerRiskId;
    @ApiModelProperty(value = "发现时间")
    private String discDate;
}
