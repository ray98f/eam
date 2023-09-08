package com.wzmtr.eam.dto.req.equipment;

import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class TransferSplitReqDTO {

    @ApiModelProperty(value = "设备列表")
    private List<EquipmentResDTO> equipmentList;

    @ApiModelProperty(value = "类型")
    private Integer type;
}
