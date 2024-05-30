package com.wzmtr.eam.dto.res.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 常用功能结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/24
 */
@Data
@ApiModel
public class CommonFuncResDTO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 路由
     */
    @ApiModelProperty(value = "路由")
    private String url;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private String sort;
}
