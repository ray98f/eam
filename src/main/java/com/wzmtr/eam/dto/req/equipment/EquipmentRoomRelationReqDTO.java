package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class EquipmentRoomRelationReqDTO {

    @ApiModelProperty(value = "设备房ID")
    private String roomId;

    @ApiModelProperty(value = "设备ID")
    private List<String> ids;
}
