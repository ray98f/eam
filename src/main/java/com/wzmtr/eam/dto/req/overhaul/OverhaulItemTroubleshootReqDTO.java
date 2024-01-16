package com.wzmtr.eam.dto.req.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulItemListReqDTO {

    @ApiModelProperty(value = "标记")
    private String objectFlag;

    @ApiModelProperty(value = "工单编号")
    private String objectCode;

    @ApiModelProperty(value = "计划编号")
    private String itemName;

    @ApiModelProperty(value = "计划名称")
    private String orderCode;

    @ApiModelProperty(value = "线路编码")
    private String objectId;

    @ApiModelProperty(value = "位置1代码")
    private String modelName;

}
