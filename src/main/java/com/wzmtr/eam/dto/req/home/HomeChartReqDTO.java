package com.wzmtr.eam.dto.req.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 首页图标查询请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/04/23
 */
@Data
@ApiModel
public class HomeChartReqDTO {
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    /**
     * 专业
     */
    @ApiModelProperty(value = "专业")
    private List<String> majors;
}
