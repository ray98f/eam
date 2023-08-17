package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class CheckPlanListReqDTO {

    @ApiModelProperty(value = "设备分类id")
    private String recId;

    @ApiModelProperty(value = "计量器具定检计划号")
    private String instrmPlanNo;

    @ApiModelProperty(value = "计划人")
    private Integer planCreaterName;

    @ApiModelProperty(value = "计划状态10:提交;20:检测中;30:完成检测")
    private String planStatus;

    @ApiModelProperty(value = "定抽检标识10:定检;20:抽检")
    private String instrmPlanType;

    @ApiModelProperty(value = "定检计划时期")
    private String planPeriodMark;

    @ApiModelProperty(value = "编制部门")
    private String editDeptCode;

    @ApiModelProperty(value = "工作流实例ID")
    private String workFlowInstId;
}
