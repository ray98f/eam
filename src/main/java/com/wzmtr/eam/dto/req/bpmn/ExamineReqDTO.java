package com.wzmtr.eam.dto.req.bpmn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamineReqDTO {

    @ApiModelProperty(value = "id")
    private String recId;

    @ApiModelProperty(value = "审核意见")
    private String opinion;

    @ApiModelProperty(value = "审核状态 0 通过 1 驳回")
    private Integer examineStatus;

    @ApiModelProperty(value = "下一步审核人 没有时为空")
    private List<String> userIds;

    private String roleId;

}
