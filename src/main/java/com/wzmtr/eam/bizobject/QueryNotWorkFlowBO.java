package com.wzmtr.eam.bizobject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class QueryNotWorkFlowBO {

    @ApiModelProperty(value = "待办id")
    private String todoId;
    
    @ApiModelProperty(value = "用户id")
    private String userId;
    
    @ApiModelProperty(value = "eip pc链接")
    private String eipUrl;
    
    @ApiModelProperty(value = "移动端链接")
    private String phoneUrl;
    private String workFlowInstId;
}
