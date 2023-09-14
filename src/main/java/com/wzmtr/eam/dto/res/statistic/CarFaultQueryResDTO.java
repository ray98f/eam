package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:59
 */
@Data
public class CarFaultQueryResDTO {
    @ApiModelProperty(value = "提报时间")
    private String fillinTime;
    @ApiModelProperty(value = "对象code")
    private String objectCode;
    @ApiModelProperty(value = "对象名")
    private String objectName;
    @ApiModelProperty(value = "数量")
    private Integer faultCount;

    private Set<String> monthData;
    private Set<String> titleData;
    private List<Map<String, Object>> tableData2;
}
