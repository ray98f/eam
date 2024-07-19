package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 设备管理-设备台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/07/11
 */
@Data
@ApiModel
public class EquipmentExportReqDTO {
    /**
     * ids
     */
    @ApiModelProperty(value = "ids")
    private List<String> ids;
    /**
     * 专业编号
     */
    @ApiModelProperty(value = "专业编号")
    private String majorCode;

}
