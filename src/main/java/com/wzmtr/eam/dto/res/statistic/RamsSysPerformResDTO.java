package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/22 17:14
 */
@ApiModel
@Data
public class RamsSysPerformResDTO {
    /*****各系统可靠性情况统计******/
    @ApiModelProperty(value = "moduleName")
    private String moduleName;
    @ApiModelProperty(value = "晚点故障数")
    private String numLate;
    @ApiModelProperty(value = "不适合继续服务故障数")
    private String numNos;
    @ApiModelProperty(value = "不适合继续服务合同故障数")
    private String contractZBLATE;
    @ApiModelProperty(value = "不适合继续服务合同指标")
    private String contractZBNOS;
    @ApiModelProperty(value = "晚点MTBF")
    private String MTBF_LATE;
    @ApiModelProperty(value = "是否达标---故障数")
    private String isDB_LATE;
    @ApiModelProperty(value = "是否达标---继续服务")
    private String isDB_NOS;
    @ApiModelProperty(value = "不适合继续服务MTBF")
    private String MTBF_NOS;
}
