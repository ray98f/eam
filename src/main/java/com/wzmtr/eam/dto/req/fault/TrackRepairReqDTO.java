package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 20:22
 */
@Data
public class TrackRepairReqDTO {

    @ApiModelProperty(value = "记录Id")
    private String recId;
    @ApiModelProperty(value = "维修部门编号")
    private String workerGroupCode;
    @ApiModelProperty(value = "记录状态")
    private String recStatus;
    @ApiModelProperty(value = "维修人")
    private String trackReporterId;
    @ApiModelProperty(value = "派工人")
    private String dispatchUserId;
    @ApiModelProperty(value = "派工时间")
    private String dispatchTime;
}
