package com.wzmtr.eam.dto.req.mea;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class MeaInfoQueryReqDTO {

    @ApiModelProperty(value = "计量器具代码列表")
    private List<String> equipCodeList;
    
    @ApiModelProperty(value = "计量器具代码")
    private String equipCode;
    
    @ApiModelProperty(value = "计量器具名称")
    private String equipName;
    
    @ApiModelProperty(value = "型号规格")
    private String matSpecifi;

    private String planBeginBeginDate;

    private String planBeginEndDate;

    private String planEndBeginDate;

    private String planEndEndDate;
}
