package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 20:22
 */
@Data
public class TrackReportReqDTO {

    @ApiModelProperty(value = "记录Id")
    private String recId;
    @ApiModelProperty(value = "故障跟踪人",required = false)
    private String trackReporterId;
    @ApiModelProperty(value = "故障跟踪编号")
    private String faultTrackNo;
    @ApiModelProperty(value = "故障跟踪报告时间",required = false)
    private String trackReportTime;
    @ApiModelProperty(value = "故障跟踪工单号")
    private String faultTrackWorkNo;
    @ApiModelProperty(value = "跟踪结果")
    private String trackResult;
    @ApiModelProperty(value = "记录状态",required = false)
    private String recStatus;
}
