package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 15:11
 */
@Data
@Builder
public class SidEntity {
    @NotNull
    @ApiModelProperty(value = "id")
    private String id;
}
