package com.wzmtr.eam.dto.req.overhaul;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检修工单流程请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/28
 */
@Data
public class OverhaulOrderFlowReqDTO {
    /**
     * id编号
     */
    @ApiModelProperty(value = "id编号")
    private String recId;
    /**
     * 工单编号
     */
    @ApiModelProperty(value = "工单编号")
    private String orderCode;
    /**
     * 工单状态
     */
    @ApiModelProperty(value = "工单状态")
    private String workStatus;
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
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private String operateTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
