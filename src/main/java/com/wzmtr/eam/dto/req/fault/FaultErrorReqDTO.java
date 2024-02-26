package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 故障错误入参
 * @author  Ray
 * @version 1.0
 * @date 2024/02/19
 */
@Data
public class FaultErrorReqDTO {
    /**
     * id编号
     */
    @ApiModelProperty(value = "id编号")
    private String recId;
    /**
     * 故障编号
     */
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    /**
     * 故障工单编号
     */
    @ApiModelProperty(value = "故障工单编号")
    private String faultWorkNo;
    /**
     * 故障信息
     */
    @ApiModelProperty(value = "故障信息")
    private String faultInfo;
    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private String recCreator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private String recCreateTime;
    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;
}
