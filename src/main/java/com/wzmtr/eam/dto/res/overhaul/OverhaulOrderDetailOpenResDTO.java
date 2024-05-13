package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulOrderDetailOpenResDTO {
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    
    @ApiModelProperty(value = "模板编号")
    private String templateId;
    
    @ApiModelProperty(value = "模板名称")
    private String templateName;
}
