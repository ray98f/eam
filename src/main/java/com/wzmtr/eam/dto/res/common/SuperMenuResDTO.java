package com.wzmtr.eam.dto.res.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class SuperMenuResDTO {

    @ApiModelProperty(value = "目录id")
    private String id;

    @ApiModelProperty(value = "目录名称")
    private String name;

    @ApiModelProperty(value = "菜单信息")
    private List<MenuInfo> menuInfo;

    @Data
    public static class MenuInfo {
        @ApiModelProperty(value = "菜单id")
        private String id;

        @ApiModelProperty(value = "菜单名称")
        private String name;
    }
}
