package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 20:22
 */
@Data
public class TrackCloseReqDTO {

    @ApiModelProperty(value = "记录Id")
    private String recId;
    @ApiModelProperty(value = "故障跟踪关闭人")
    private String trackCloserId;
    @ApiModelProperty(value = "故障跟踪编号")
    private String faultTrackNo;
    @ApiModelProperty(value = "故障跟踪关闭时间")
    private String trackCloseTime;
    @ApiModelProperty(value = "故障跟踪工单号")
    private String faultTrackWorkNo;
    @ApiModelProperty(value = "记录状态")
    private String recStatus;
}
