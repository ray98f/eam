package com.wzmtr.eam.dto.res.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class MenuDetailResDTO {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "父菜单名称")
    private String parentName;

    @ApiModelProperty(value = "目录名称")
    private String name;

    @ApiModelProperty(value = "权限类型 1目录、2菜单、3按钮")
    private Integer type;

    @ApiModelProperty(value = "目录图标")
    private String icon;

    @ApiModelProperty(value = "目录排序")
    private Integer sort;

    @ApiModelProperty(value = "路由地址")
    private String url;

    @ApiModelProperty(value = "权限编码,如:article:create、article:edit等")
    private String permCode;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "状态 0:正常,9:禁用")
    private Integer status;

    @ApiModelProperty(value = "是否显示 0:隐藏,1显示")
    private Integer isShow;

}
