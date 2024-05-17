package com.wzmtr.eam.dto.req.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计分析-屏蔽门故障信息传参类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/17
 */
@ApiModel
@Data
public class DoorFaultReqDTO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String recId;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String month;
    /**
     * 故障次数
     */
    @ApiModelProperty(value = "故障次数")
    private Long faultNum;
    /**
     * 动作次数
     */
    @ApiModelProperty(value = "动作次数")
    private Long actionNum;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String recCreator;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String recCreateTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private String recRevisor;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String recReviseTime;
}
