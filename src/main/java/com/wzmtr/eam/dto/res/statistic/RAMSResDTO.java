package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/22 17:14
 */
@Data
public class RAMSResDTO {
    private String faultNum;
    private String affect11;
    private String affect21;
    private String millionMiles;
    private String affect11Performance;
    private String affect21Performance;


    /*******/
    @ApiModelProperty(value = "不适合服务")
    private String noService;
    @ApiModelProperty(value = "年月")
    private String yearMonth;
    private String late5to15;
    private String late15;
    @ApiModelProperty(value = "晚点")
    private String late;
    @ApiModelProperty(value = "总数")
    private String sum;
    @ApiModelProperty(value = "总公里数")
    private String miles;
    /***********/
    @ApiModelProperty(value = "moduleName")
    private String moduleName;
    @ApiModelProperty(value = "NUM_LATE")
    private String numLate;
    @ApiModelProperty(value = "NUM_NOS")
    private String numNos;
    private String contractZBLATE;
    private String contractZBNOS;
}
