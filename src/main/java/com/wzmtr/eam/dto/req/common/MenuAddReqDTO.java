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
public class MenuAddReqDTO {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "目录名称")
    @NotBlank(message = "32000006")
    private String name;

    @ApiModelProperty(value = "权限类型 1目录 2菜单 3按钮")
    @NotNull(message = "32000006")
    private Integer type;

    @ApiModelProperty(value = "目录图标")
    private String icon;

    @ApiModelProperty(value = "目录排序")
    @NotNull(message = "32000006")
    private Integer sort;

    @ApiModelProperty(value = "路由地址")
    private String url;

    @ApiModelProperty(value = "权限编码,如:article:create、article:edit等")
    private String permCode;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "状态 0:正常,9:禁用")
    @NotNull(message = "32000006")
    private Integer status;

    @ApiModelProperty(value = "是否显示 0:隐藏,1显示")
    @NotNull(message = "32000006")
    private Integer isShow;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "用户id")
    private String userId;
}
