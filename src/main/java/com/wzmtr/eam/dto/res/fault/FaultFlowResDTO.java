package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 故障流程结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/02/05
 */
@Data
public class FaultFlowResDTO {
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
     * 工单状态
     */
    @ApiModelProperty(value = "工单状态")
    private String orderStatus;
    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private String operateUserId;
    /**
     * 操作人名
     */
    @ApiModelProperty(value = "操作人名")
    private String operateUserName;
    /**
     * 操作人手机号
     */
    @ApiModelProperty(value = "操作人手机号")
    private String operateUserMobile;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private String operateTime;
}
