package com.wzmtr.eam.dto.res.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 故障数量统计结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/24
 */
@Data
@ApiModel
public class SubjectFaultResDTO {
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号")
    private String subjectCode;
    /**
     * 系统名称
     */
    @ApiModelProperty(value = "系统名称")
    private String subjectName;
    /**
     * 故障数量
     */
    @ApiModelProperty(value = "故障数量")
    private Long faultNum;
}