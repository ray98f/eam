package com.wzmtr.eam.dto.res.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 14:27
 */
@Data
@ApiModel
public class HomeCountResDTO {
    @ApiModelProperty(value = "待办数量")
    private String todoSize;
    @ApiModelProperty(value = "待阅数量")
    private String messageSize;
    @ApiModelProperty(value = "已办数量")
    private String overSize;
    @ApiModelProperty(value = "已阅数量")
    private String readSize;
}
