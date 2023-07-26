package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkFlow {
    @ApiModelProperty(value = "待办id")
    private String todoId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "eip链接")
    private String eipUrl;

    @ApiModelProperty(value = "eip手机端链接")
    private String phoneUrl;

}
