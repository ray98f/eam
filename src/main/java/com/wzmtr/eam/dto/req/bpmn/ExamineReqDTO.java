package com.wzmtr.eam.dto.req.bpmn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamineReqDTO {

    @ApiModelProperty(value = "id")
    private String recId;

    @ApiModelProperty(value = "审核意见")
    private String opinion;

    @ApiModelProperty(value = "审核状态 0 通过 1 驳回")
    private Integer examineStatus;

}
