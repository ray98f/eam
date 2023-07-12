package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class UserRoleDetailResDTO {

    @ApiModelProperty(value = "用户的角色列表")
    private List<UserRoleResDTO> useRole;

    @ApiModelProperty(value = "所有角色列表")
    private List<UserRoleResDTO> roles;

}
