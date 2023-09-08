package com.wzmtr.eam.dto.res.equipment;

import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.basic.EquipmentCategoryResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class EquipmentTreeResDTO {

    @ApiModelProperty(value = "线路")
    private List<RegionResDTO> line;

    @ApiModelProperty(value = "位置")
    private List<RegionResDTO> region;

    @ApiModelProperty(value = "设备分类")
    private List<EquipmentCategoryResDTO> equipment;
}
