package com.wzmtr.eam.dto.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class RegionReqDTO {

    @ApiModelProperty(value = "设备分类id")
    private String recId;

    @ApiModelProperty(value = "父节点id")
    private String parentNodeRecId;

    @ApiModelProperty(value = "级别")
    private Integer nodeLevel;

    @ApiModelProperty(value = "设备分类节点编号")
    private String nodeCode;

    @ApiModelProperty(value = "设备分类节点名称")
    private String nodeName;

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
