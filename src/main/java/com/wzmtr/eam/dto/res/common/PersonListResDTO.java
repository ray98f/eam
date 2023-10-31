package com.wzmtr.eam.dto.res.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PersonListResDTO {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    private String userLevel;

}
