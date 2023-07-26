package com.wzmtr.eam.dto.req;

import com.wzmtr.eam.dto.res.EquipmentResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
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
