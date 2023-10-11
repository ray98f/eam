package com.wzmtr.eam.dto.req.spareAndCarVideo;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/7 11:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class CarVideoReqDTO extends PageReqDTO {
    @ApiModelProperty(value = "调阅记录号")
    private String applyNo;
    @ApiModelProperty(value = "申请调阅时间开始")
    private String startApplyTime;
    @ApiModelProperty(value = "申请调阅时间结束")
    private String endApplyTime;
    @ApiModelProperty(value = "状态")
    private String recStatus;
}
