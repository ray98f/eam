package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class EquipmentCategoryResDTO {

    @ApiModelProperty(value = "设备分类id")
    private String recId;

    @ApiModelProperty(value = "公司编号")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "父节点id")
    private String parentNodeRecId;

    @ApiModelProperty(value = "设备分类节点编号")
    private String nodeCode;

    @ApiModelProperty(value = "设备分类节点名称")
    private String nodeName;

    @ApiModelProperty(value = "等级")
    private Integer nodeLevel;

    @ApiModelProperty(value = "记录状态 10 启用 20 禁用")
    private String recStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "子节点")
    private List<EquipmentCategoryResDTO> children;

    private String lineCode;

    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
}
