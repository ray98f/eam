package com.wzmtr.eam.dto.res.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class RegionResDTO {

    @ApiModelProperty(value = "设备分类id")
    private String recId;

    @ApiModelProperty(value = "父节点id")
    private String parentNodeRecId;

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

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "子节点")
    private List<RegionResDTO> children;
}
