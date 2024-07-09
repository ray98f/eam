package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 设备分类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/04
 */
@Data
@ApiModel
public class EquipmentCategory {
    /**
     * 专业编号
     */
    @ApiModelProperty(value = "专业编号")
    private List<String> majorCodes;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号")
    private List<String> systemCodes;
    /**
     * 设备类型编号
     */
    @ApiModelProperty(value = "设备类型编号")
    private List<String> equipTypeCodes;

    public EquipmentCategory(List<String> majorCodes) {
        this.majorCodes = majorCodes;
    }

    public EquipmentCategory(List<String> majorCodes, List<String> systemCodes) {
        this.majorCodes = majorCodes;
        this.systemCodes = systemCodes;
    }

    public EquipmentCategory(List<String> majorCodes, List<String> systemCodes, List<String> equipTypeCodes) {
        this.majorCodes = majorCodes;
        this.systemCodes = systemCodes;
        this.equipTypeCodes = equipTypeCodes;
    }
}
