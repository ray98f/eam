package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故障管理-故障跟踪工单导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/10
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelFaultFollowResDTO {

    @ApiModelProperty(value = "跟踪单号")
    private String followNo;
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "跟踪原因")
    private String followReason;
    @ApiModelProperty(value = "跟踪起止时间")
    private String followStartEndTime;
    @ApiModelProperty(value = "跟踪周期（天）")
    private Integer followCycle;
    @ApiModelProperty(value = "跟踪状态")
    private String followStatus;
    @ApiModelProperty(value = "发起人")
    private String followUserName;
    @ApiModelProperty(value = "发起时间")
    private String followTime;
    @ApiModelProperty(value = "关闭人")
    private String followCloserName;
    @ApiModelProperty(value = "关闭时间")
    private String followCloseTime;
}
