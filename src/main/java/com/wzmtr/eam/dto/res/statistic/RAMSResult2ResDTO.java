package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/22 17:14
 */
@Data
@ApiModel
public class RAMSResult2ResDTO {

    @ApiModelProperty(value = "不适合服务")
    private String noService;
    @ApiModelProperty(value = "年月")
    private String yearMonth;
    @ApiModelProperty(value = "晚点")
    private String late;
    @ApiModelProperty(value = "总数")
    private String sum;
    @ApiModelProperty(value = "实际车公里数")
    private String miles;

}
