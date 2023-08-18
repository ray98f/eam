package com.wzmtr.eam.dto.req.statistic;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:36
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class CarFaultQueryReqDTO extends PageReqDTO {
    @ApiModelProperty(value = "车辆codes")
    private Set<String> objectCode;
    @ApiModelProperty(value = "时间周期开始")
    private String startTime;
    @ApiModelProperty(value = "时间周期结束")
    private String endTime;
}
