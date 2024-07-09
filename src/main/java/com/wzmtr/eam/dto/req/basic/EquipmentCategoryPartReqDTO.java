package com.wzmtr.eam.dto.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备分类部件请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/07/04
 */
@Data
@ApiModel
public class EquipmentCategoryPartReqDTO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String recId;
    /**
     * 专业编号
     */
    @ApiModelProperty(value = "专业编号")
    private String majorCode;
    /**
     * 专业名称
     */
    @ApiModelProperty(value = "专业名称")
    private String majorName;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号")
    private String systemCode;
    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String systemName;
    /**
     * 设备类型编号
     */
    @ApiModelProperty(value = "设备类型编号")
    private String equipTypeCode;
    /**
     * 设备类型名称
     */
    @ApiModelProperty(value = "设备类型名称")
    private String equipTypeName;
    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")
    private String moduleName;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String partName;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Long quantity;
    /**
     * 新增人
     */
    @ApiModelProperty(value = "新增人")
    private String recCreator;
    /**
     * 新增时间
     */
    @ApiModelProperty(value = "新增时间")
    private String recCreateTime;
}
