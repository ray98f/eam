package com.wzmtr.eam.dto.req.video;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 11:06
 */
@Data
@ApiModel
public class CarVideoExportReqDTO {
    @ApiModelProperty(value = "调阅记录号")
    private Set<String> applyNo;
    @ApiModelProperty(value = "申请调阅时间开始")
    private String startApplyTime;
    @ApiModelProperty(value = "申请调阅时间结束")
    private String endApplyTime;
    @ApiModelProperty(value = "状态")
    private String recStatus;
}
