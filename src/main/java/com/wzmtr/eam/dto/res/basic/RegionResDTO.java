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

    /**
     * 设备分类id
     */
    @ApiModelProperty(value = "设备分类id")
    private String recId;
    /**
     * 父节点id
     */
    @ApiModelProperty(value = "父节点id")
    private String parentNodeRecId;
    /**
     * 设备分类节点编号
     */
    @ApiModelProperty(value = "设备分类节点编号")
    private String nodeCode;
    /**
     * 设备分类节点名称
     */
    @ApiModelProperty(value = "设备分类节点名称")
    private String nodeName;
    /**
     * 记录状态 10 启用 20 禁用
     */
    @ApiModelProperty(value = "记录状态 10 启用 20 禁用")
    private String recStatus;
    /**
     * 线路编号 01 S1线 02 S2线
     */
    @ApiModelProperty(value = "线路编号 01 S1线 02 S2线")
    private String lineCode;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String recCreator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;
    /**
     * 子节点
     */
    @ApiModelProperty(value = "子节点")
    private List<RegionResDTO> children;
}
