package com.wzmtr.eam.dto.req.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author frp
 */
@Data
@ApiModel
public class UserStatusReqDTO {

    @ApiModelProperty(value = "主键id")
    @NotBlank(message = "32000006")
    private String id;

    @ApiModelProperty(value = "锁定状态 0 正常 1 锁定")
    @NotNull(message = "32000006")
    private Integer disabled;

    @ApiModelProperty(value = "新增人")
    private String createdBy;
}
