package com.wzmtr.eam.dto.req.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class UserRoleReqDTO {

    @ApiModelProperty(value = "权限id")
    private String roleId;

    @ApiModelProperty(value = "用户id")
    private List<String> userIds;
}
