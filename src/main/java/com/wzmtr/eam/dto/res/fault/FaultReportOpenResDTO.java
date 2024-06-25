package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 故障提报开放接口返回类
 * @author  Ray
 * @version 1.0
 * @date 2024/06/25
 */
@Data
public class FaultReportOpenResDTO {
    /**
     * 故障编号
     */
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    /**
     * 故障工单号
     */
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
}