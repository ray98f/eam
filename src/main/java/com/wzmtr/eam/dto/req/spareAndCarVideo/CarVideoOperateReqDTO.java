package com.wzmtr.eam.dto.req.spareAndCarVideo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 11:06
 */
@Data
@ApiModel
public class CarVideoOperateReqDTO {
    @ApiModelProperty(value = "需要执行的记录状态,20-下达，30-派工，40-完工，50-关闭")
    private String recStatus;
    @ApiModelProperty(value = "调阅记录号")
    private String recId;
    @ApiModelProperty(value = "调度人")
    private String dispatchUserId;
    @ApiModelProperty(value = "维修人。如有多个则','隔开")
    private String workerId;
    @ApiModelProperty(value = "工班")
    private String workClass;

}
