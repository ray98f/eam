package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class OrgParentResDTO {

    private String id;

    @ApiModelProperty(value = "父ID路径")
    private String parentIds;

    @ApiModelProperty(value = "名称路径")
    private String names;
}
