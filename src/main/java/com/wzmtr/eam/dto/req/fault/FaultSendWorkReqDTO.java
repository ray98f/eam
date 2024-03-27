package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 10:35
 */
@ApiModel
@Data
public class FaultSendWorkReqDTO {
    @ApiModelProperty(value = "维修工班组code")
    private String workerGroupCode;
    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;
    @ApiModelProperty(value = "faultInfo表RecId")
    private String faultInfoRecId;
    @ApiModelProperty(value = "order表recId")
    private String faultOrderRecId;
    @ApiModelProperty(value = "维修负责人部门code")
    private String repairDeptCode;
    @ApiModelProperty(value = "维修负责人ID")
    private String repairRespUserId;
    @ApiModelProperty(value = "维修负责人")
    private String repairRespUserName;
    @ApiModelProperty(value = "派工时间")
    private String dispatchTime;
    @ApiModelProperty(value = "派工人")
    private String dispatchName;
    @ApiModelProperty(value = "工作区域")
    private String workArea;
    @ApiModelProperty(value = "预计恢复时间")
    private String planRecoveryTime;
    @ApiModelProperty(value = "工班")
    private String workClass;
    @ApiModelProperty(value = "专业Code")
    private String majorCode;
    private String isTiKai;
    @ApiModelProperty(value = "故障影响")
    private String faultAffect;
    private String recReviseTime;
    private String recRevisor;
    private String ext1;
    @ApiModelProperty(value = "故障紧急程度")
    private String faultLevel;
}
