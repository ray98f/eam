package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class SubmissionListReqDTO {

    @ApiModelProperty(value = "id")
    private String recId;

    @ApiModelProperty(value = "送检单号")
    private String sendVerifyNo;

    @ApiModelProperty(value = "计量器具检定计划号")
    private Integer instrmPlanNo;

    @ApiModelProperty(value = "送检单状态10:送检中;20:完成检测;")
    private String sendVerifyStatus;

    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
}
