package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class FaultReqDTO {

    @ApiModelProperty(value = "设备分类id")
    private String recId;

    @ApiModelProperty(value = "设备分类编号")
    private String equipmentTypeCode;

    @ApiModelProperty(value = "码值类型 1 现象码 2 原因码 3 行动码")
    private String faultCodeType;

    @ApiModelProperty(value = "编号")
    private String faultCode;

    @ApiModelProperty(value = "描述")
    private String faultDescr;

    @ApiModelProperty(value = "关联码值")
    private String relatedCodes;

    @ApiModelProperty(value = "记录状态 10 启用 20 禁用")
    private String recStatus;

    @ApiModelProperty(value = "线路编号 01 S1线 02 S2线")
    private String lineCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String recCreator;

    @ApiModelProperty(value = "修改人")
    private String recRevisor;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;
}
