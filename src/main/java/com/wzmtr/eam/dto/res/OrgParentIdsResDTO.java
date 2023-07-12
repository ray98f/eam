package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class OrgParentIdsResDTO {

    private String id;

    @ApiModelProperty(value = "父ID路径")
    private String parentIds;
}
