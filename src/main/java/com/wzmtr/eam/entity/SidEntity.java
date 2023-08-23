package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 15:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SidEntity {
    @NotNull
    @ApiModelProperty(value = "id")
    private String id;
}
